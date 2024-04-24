# 이력썰 - Ressul
- 팀명 : Ressul Company
- 프로젝트 명 : 이력썰 (Ressul)
- 프로젝트 소개 : 취준생들이 이력서를 통해 썰을 푸는 커뮤니티 웹 사이트
- 프로젝트 계기 : 제작 당시 부트캠프, 수료할 날이 얼마 남지 않았고 취업을 하기 위해서는 이력서나 포트폴리오를 작성해야 했다.. <br>
  최근 개발자들의 포트폴리오나 이력서가 정말 다양한 형태로 작성되고 있어 다른 사람들의 이력서를 편하게 확인하고 얘기하는 공간이 필요하다고 생각되어 만들게 됐다.
 ---

## 사용 기술
 * 언어: <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
 * 프레임워크: <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
 * 데이터베이스: <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
 * ORM: JPA
 ---
## ERD
<img src="https://github.com/RessulCompany/ressul/assets/24753709/8dff180c-7f21-42a8-b957-40ca90c50667">

 ---

## 와이어프레임
<img src="https://github.com/RessulCompany/ressul/assets/24753709/0ed2215e-7519-4ad0-b42b-2adec7bd1661">

 ---

## 프로젝트 기능
 * 선착순 이벤트 기능
 * OAuth2 회원 기능
 * 이력서 캐싱 기능
 * 회원권 결제 기능
 ---

## 담당 역할
* 선착순 이벤트 CRUD
* 동시성 문제를 해결하기 위해 Redis를 활용해 Spin Lock 사용
* Lock이 제대로 작동 되는 지 확인하기 위해 스레드 풀을 활용한 테스트 케이스 작성
 ---
### Spin Lock 사용
 * 이력서를 무료로 리뷰해주는 이벤트 내에서 동시성 문제 발생했기에 Lock을 사용하기로 했다.
 * 2주 간의 짧은 프로젝트 기간으로 인해 러닝 커브 우려로 Spin Lock 사용을 결정했다.
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
> Redis를 사용하는 Repository이다.

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
> 동시성 문제를 해결하기 위해 Lock을 걸었다

### 테스트 코드와 데이터 삭제 문제
- 데이터베이스 내의 테스트 데이터가 사라지지 않는 문제 발생했다.
- Soft-Delete로 적용으로 인해 테스트 코드 내에서 deleteAll을 하여도 실제로 데이터가 삭제 되지 않았다.
- Profile을 활용해서 테스트 코드의 경우에만 H2 데이터베이스 사용을 결정하였다.
  
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

        describe("여러 개의 Thread가")
        {
            context("하나의 Event에 거의 동시에 참여하면")
            {
                it("정확한 인원이 참여한다.")
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
> 스레드 풀을 활용한 테스트 코드
---

## 아쉬운 점
 * 내가 사용한 Lettuce의 Spin Lock 방식은 서버에 부담을 가게 하는 방식이다. 이 프로젝트를 통해 Redis를 처음 배워서, Redisson까지 사용하기엔 시간이 없었기에 간단한 Spin Lock 방식을 사용했지만
만약 다음에 동시성 문제를 해결한다고 한다면 분산 락 방식을 사용을 해보고 싶다.
---

## 발표 자료
- https://www.canva.com/design/DAF9f_pgIdc/JBcKHnPL2x-FVT1YyN9ZYA/edit?utm_content=DAF9f_pgIdc&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton
