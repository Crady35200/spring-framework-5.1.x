/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.framework.adapter;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.util.Assert;

/**
 * Interceptor to wrap am {@link org.springframework.aop.MethodBeforeAdvice}.
 * Used internally by the AOP framework; application developers should not need
 * to use this class directly.
 *
 * @author Rod Johnson
 * @see AfterReturningAdviceInterceptor
 * @see ThrowsAdviceInterceptor
 */
@SuppressWarnings("serial")
public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice, Serializable {

	private final MethodBeforeAdvice advice;


	/**
	 * Create a new MethodBeforeAdviceInterceptor for the given advice.
	 * @param advice the MethodBeforeAdvice to wrap
	 */
	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		Assert.notNull(advice, "Advice must not be null");
		this.advice = advice;
	}


	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		//终于开始做事了，调用增强器的before方法，明显是通过反射的方式调用
		//到这里增强方法before的业务逻辑执行
		this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
		//又调用了调用MethodInvocation的proceed方法
		//我们知道这是最后一个拦截器了，此时this.currentInterceptorIndex ==
		// this.interceptorsAndDynamicMethodMatchers.size() - 1应该为true了，
		// 那么就会执行 return invokeJoinpoint();，也就是执行bean中的目标方法
		return mi.proceed();
	}

}
