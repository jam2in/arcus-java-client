/*
 * arcus-java-client : Arcus Java client
 * Copyright 2010-2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.spy.memcached.collection.btree;

import java.util.concurrent.TimeUnit;

import net.spy.memcached.collection.BaseIntegrationTest;
import net.spy.memcached.collection.CollectionResponse;
import net.spy.memcached.internal.CollectionFuture;

public class BopMutateTest extends BaseIntegrationTest {

	private String key = "BopMutate";

	private String[] items9 = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a" };

	protected void setUp() {
		try {
			super.setUp();
			mc.delete(key);
		} catch (Exception ignored) {
		}
	}

	public void testBopIncrDecr_Basic() throws Exception {
		// Create a list and add it 9 items
		addToBTree(key, items9);

		// incr 2
		assertTrue(mc.asyncBopIncr(key, 1L, 2)
				.get(1000, TimeUnit.MILLISECONDS).equals(4L));
		// incr 10
		assertTrue(mc.asyncBopIncr(key, 1L, 10)
				.get(1000, TimeUnit.MILLISECONDS).equals(14L));

		// decr 1
		assertTrue(mc.asyncBopDecr(key, 1L, 1)
				.get(1000, TimeUnit.MILLISECONDS).equals(13L));
		// decr 11
		assertTrue(mc.asyncBopDecr(key, 1L, 11)
				.get(1000, TimeUnit.MILLISECONDS).equals(2L));
		// decr 4
		assertTrue(mc.asyncBopDecr(key, 1L, 4)
				.get(1000, TimeUnit.MILLISECONDS).equals(0L));
	}

	public void testBopIncrDecr_Minus() throws Exception {
		// Create a list and add it 9 items
		addToBTree(key, items9);

		// decr 10
		assertTrue(mc.asyncBopDecr(key, 1L, 10)
				.get(1000, TimeUnit.MILLISECONDS).equals(0L));
	}

	public void testBopIncrDecr_NoKeyError() throws Exception {
		// Create a list and add it 9 items
		addToBTree(key, items9);

		// not exists the key
		CollectionFuture<Long> future = mc.asyncBopIncr("aaaaa", 0L, 2);
		Long result = future.get(1000, TimeUnit.MILLISECONDS);
		CollectionResponse response = future.getOperationStatus().getResponse();
		assertTrue(response.toString() == "NOT_FOUND");

		// not exists the bkey
		CollectionFuture<Long> future2 = mc.asyncBopIncr(key, 10L, 2);
		Long result2 = future2.get(1000, TimeUnit.MILLISECONDS);
		CollectionResponse response2 = future2.getOperationStatus()
				.getResponse();
		assertTrue(response2.toString() == "NOT_FOUND_ELEMENT");
	}

	public void testBopIncrDecr_StringError() throws Exception {
		// Create a list and add it 9 items
		addToBTree(key, items9);

		try {
			// incr string value
			CollectionFuture<Long> future3 = mc.asyncBopIncr(key, 9L, 2);
			Long result3 = future3.get(1000, TimeUnit.MILLISECONDS);
			CollectionResponse response3 = future3.getOperationStatus()
					.getResponse();
			System.out.println(response3.toString());
		} catch (Exception e) {
			assertEquals(
					"OperationException: CLIENT: CLIENT_ERROR cannot increment or decrement non-numeric value",
					e.getMessage());
		}
	}
}