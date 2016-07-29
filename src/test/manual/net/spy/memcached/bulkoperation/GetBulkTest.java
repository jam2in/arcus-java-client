/*
 * arcus-java-client : Arcus Java client
 * Copyright 2016 JaM2in Corp.
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
package net.spy.memcached.bulkoperation;

import net.spy.memcached.collection.BaseIntegrationTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GetBulkTest extends BaseIntegrationTest {
	public void testOperationTime() {
		Collection<String> keys = new ArrayList<String>();
		int keyListSize = 100;
		String value;

		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			b.append("b");
		}
		value = b.toString();

		for (int i = 0; i < keyListSize; i++) {
			StringBuilder a = new StringBuilder();
			for (int j = 0; j < 50; j++) {
				a.append("a");
			}
			a.append(i);
			if (i < 5) {
				mc.set(a.toString(), 0, value);
			}
			keys.add(a.toString());
		}

		Future<Map<String, Object>> future = null;
		Future<Map<String, Object>> future2 = null;
		Map<String, Object> result = null;
		Map<String, Object> result2 = null;

		// first test
		try {
			Long start1 = System.currentTimeMillis();

			future = mc.asyncGetBulk(keys);

			Long end1_1 = System.currentTimeMillis();

			result = future.get(1000L, TimeUnit.MILLISECONDS);

			Long end1_2 = System.currentTimeMillis();
			System.out.println("getBulk origin : " + (end1_1 - start1));
			System.out.println("future.get origin : " + (end1_2 - end1_1));
			for(Entry<String, Object> entry : result.entrySet()) {
				System.out.println("key = "+entry.getKey());
				System.out.println("value = "+entry.getValue());
			}
		} catch (InterruptedException e) {
			future.cancel(true);
		} catch (TimeoutException e) {
			future.cancel(true);
		} catch (ExecutionException e) {
			future.cancel(true);
		}

		//second test
		try {
			Long start2 = System.currentTimeMillis();

			future2 = mc.asyncGetBulk2(keys);

			Long end2_1 = System.currentTimeMillis();

			result2 = future2.get(1000L, TimeUnit.MILLISECONDS);

			Long end2_2 = System.currentTimeMillis();
			System.out.println("getBulk new : " + (end2_1 - start2));
			System.out.println("future.get new : " + (end2_2 - end2_1));
			for(Entry<String, Object> entry : result2.entrySet()) {
				System.out.println("key = "+entry.getKey());
				System.out.println("value = "+entry.getValue());
			}
		} catch (InterruptedException e) {
			future2.cancel(true);
		} catch (TimeoutException e) {
			future2.cancel(true);
		} catch (ExecutionException e) {
			future2.cancel(true);
		}
	}
}
