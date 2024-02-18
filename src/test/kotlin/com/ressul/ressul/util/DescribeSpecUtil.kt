package com.ressul.ressul.util

import io.kotest.core.spec.style.scopes.DescribeSpecContainerScope
import io.kotest.core.test.TestScope
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

suspend fun DescribeSpecContainerScope.flushIt(
	name: String,
	em: TestEntityManager,
	beforeTest: BeforeTest? = null,
	test: suspend TestScope.() -> Unit
) {
	this.it(name) {
		beforeTest?.invoke()
		test.invoke(this)
		em.flush()
	}
}

typealias BeforeTest = () -> Unit