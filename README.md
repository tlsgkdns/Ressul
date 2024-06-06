# Ressul
- チーム名 : Ressul Company
- プロジェクト名 : Ressul
- プロジェクト紹介 : 就活生が履歴書を通して話をする空間
- プロジェクトのきっかけ :製作当時、ブートキャンプ、修了する日があまり残っておらず、就職するためには履歴書やポートフォリオを作成しなければならなかった。 <br>
  最近、開発者のポートフォリオや履歴書が本当に多様な形で作成されており、他の人の履歴書を気楽に確認して話す空間が必要だと考え、作ることになった。
 ---

## 使用技術
 * 言語: <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
 * フレームワーク: <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
 * データベース: <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
 * ORM: JPA
 ---
## ERD
<img src="https://github.com/RessulCompany/ressul/assets/24753709/8dff180c-7f21-42a8-b957-40ca90c50667">

 ---

## ワイヤフレーム
<img src="https://github.com/RessulCompany/ressul/assets/24753709/0ed2215e-7519-4ad0-b42b-2adec7bd1661">

 ---

## プロジェクト機能
 * 先着順イベント機能
 * OAuth2会員機能
 * 履歴書キャッシング機能
 * 会員券決済機能
 ---

## 担当パート
* 先着順イベントCRUD
* 同時性問題を解決するためにRedisを活用してSpin Lockを使用
* ロックが正常に作動するか確認するためにスレッドプールを活用したテストケース作成
 ---
### Spin Lock 使用
 * 履歴書を無料でレビューしてくれるイベント内で、同時性問題の発生が懸念され、Lockを使用することにした
 * 2週間の短いプロジェクト期間により、ランニングカーブの懸念でSpin Lockの使用を決定した
```kotlin
@Component
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private fun makeKey(name: String, id: Long): String
    {
        return name + "_" + id.toString()
    }
    fun lock(name: String, id:Long) : Boolean
    {
        return redisTemplate.opsForValue()
            .setIfAbsent(makeKey(name, id), "lock", Duration.ofMillis(3_000)) ?: false
    }
    fun unlock(name: String, id: Long): Boolean
    {
        return redisTemplate.delete(makeKey(name, id))
    }
}
```
> Redis を使用するRepository

```kotlin
override fun participateEvent(eventId: Long, loginMember: LoginMember): ParticipantsResponse {
        while (!redisLockRepository.lock("Event", eventId))
        {
            Thread.sleep(100)
        }
        try {
            val event = getExistEvent(eventId)
            if(participantRepository.existsParticipantByEventIdAndMemberId(eventId, loginMember.id))
                throw AlreadyParticipatedEvent(ErrorCode.ALREADY_PARTICIPATED_EVENT)
            if(!event.addParticipant()) throw EventIsClosedException(ErrorCode.EVENT_IS_CLOSED)
            eventRepository.save(event)
            return participantRepository.save(Participant(member = memberRepository.findByIdOrNull(loginMember.id), event = event))
                .let { ParticipantsResponse.from(it) }
        } finally {
            redisLockRepository.unlock("Event", eventId)
        }
    }

```
> 同時性問題を解決するためにLockをかけた

### テストコードとデータ消去問題
- データベース内のテストデータが消えない問題が発生
- Soft-Deleteの適用により、テストコード内でdeleteAllをしても実際にデータが削除されなかった。
- Profile を活用し、テストコードの場合のみH2 データベースの使用を決定
  
```kotlin
@ActiveProfiles("test")
@SpringBootTest
class RessulEventServiceTests @Autowired constructor(
    private val redisLockRepository: RedisLockRepository,
    private val participantRepository: ParticipantRepository,
    private val memberRepository: MemberRepository,
    private val eventRepository: EventRepository
) : DescribeSpec(
    {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

        describe("多数のThreadが")
        {
            context("一つのEventへ ほぼ同時に参加したら")
            {
                it("正確な人数が参加する")
                {
                    val threadCount = 100
                    addRandomMember(memberRepository, threadCount)
                    val eventService = EventServiceImpl(eventRepository = eventRepository, memberRepository = memberRepository,
                        redisLockRepository = redisLockRepository, participantRepository = participantRepository)
                    val eventId = eventRepository.save(EventEntity("This is tutor", 100)).id!!
                    val executorService = Executors.newFixedThreadPool(20)
                    val countDownLatch = CountDownLatch(threadCount)
                    repeat(threadCount)
                    {
                        executorService.submit{
                            try{
                                eventService.participateEvent(eventId, loginMember = LoginMember(it.toLong() + 1, "aaa"))
                            } finally {
                                countDownLatch.countDown()
                            }
                        }
                    }
                    countDownLatch.await()
                    eventRepository.findByIdOrNull(eventId)!!.participantCount shouldBe threadCount
                }
            }
      }
  }
)
```
> スレッドプールを活用したテストコード
---

## 物足りない点
 * 私が使ったLettuceのSpin Lock方式はサーバーに負担をかける方式だ。 このプロジェクトを通じてRedisを初めて学んで、Redissonまで使うには時間がなかったので簡単なSpin Lock方式を使いましたが
もし、次に同時性問題を解決するとしたら、分散ロック方式を使ってみたい
---

## 発表資料
- https://www.canva.com/design/DAF9f_pgIdc/JBcKHnPL2x-FVT1YyN9ZYA/edit?utm_content=DAF9f_pgIdc&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton
