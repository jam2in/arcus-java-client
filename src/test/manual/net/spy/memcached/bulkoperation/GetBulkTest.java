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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GetBulkTest extends BaseIntegrationTest {
	public void testOperationTime() throws ExecutionException, InterruptedException {
		Collection<String> keys = new ArrayList<String>();
		int keyListSize = 10000;

		for (int i = 0; i < keyListSize; i++) {
			String key = "MyKey" + i;
			keys.add(key);
		}

		// first test
		Long start1 = System.currentTimeMillis();
		Future<Map<String, Object>> future = mc
				.asyncGetBulk(keys);
		Long end1_1 = System.currentTimeMillis();
		future.get();
		Long end1_2 = System.currentTimeMillis();
		System.out.println("getBulk origin : " + (end1_1 - start1) / 1000.0);
		System.out.println("future.get origin : " + (end1_2 - end1_1) / 1000.0);

		//second test
		Long start2 = System.currentTimeMillis();
		Future<Map<String, Object>> future2 = mc
				.asyncGetBulk2(keys);
		Long end2_1 = System.currentTimeMillis();
		future2.get();
		Long end2_2 = System.currentTimeMillis();
		System.out.println("getBulk new : " + (end2_1 - start2) / 1000.0);
		System.out.println("future.get new : " + (end2_2 - end2_1) / 1000.0);
	}
}
