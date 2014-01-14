/*
 * Copyright (c) 2013 DataTorrent, Inc. ALL Rights Reserved.
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
package com.datatorrent.lib.db;

import com.datatorrent.lib.util.KeyValPair;
import java.util.Map;

/**
 *
 * @since 0.9.3
 */
public class AggregateKeyValPairTransactionableStoreOutputOperator<K, V, S extends TransactionableKeyValueStore>
        extends AbstractAggregateTransactionableStoreOutputOperator<KeyValPair<K, V>, S>
{
  protected Map<Object, Object> dataMap;

  @Override
  public void storeAggregate()
  {
    store.putAll(dataMap);
  }

  @Override
  protected long getCommittedWindowId(String appId, int operatorId)
  {
    Object value = store.get(getCommittedWindowKey(appId, operatorId));
    return (value == null) ? -1 : Long.valueOf(value.toString());
  }

  @Override
  protected void storeCommittedWindowId(String appId, int operatorId, long windowId)
  {
    store.put(getCommittedWindowKey(appId, operatorId), windowId);
  }

  protected Object getCommittedWindowKey(String appId, int operatorId)
  {
    return "_dt_wid:" + appId + ":" + operatorId;
  }

  @Override
  public void processTuple(KeyValPair<K, V> tuple)
  {
    dataMap.put(tuple.getKey(), tuple.getValue());
  }

}
