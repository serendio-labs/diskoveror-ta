/*******************************************************************************
 *   Copyright 2015 Serendio Inc. ( http://www.serendio.com/ )
 *   Author - Praveen Jesudhas
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/

package com.diskoverorta.coreference;

/**
 * An operator which compares two values for similarity, and returns a
 * number in the range 0.0 to 1.0 indicating the degree of similarity.
 */
public interface Comparator {

  /**
   * Returns true if the comparator breaks string values up into
   * tokens when comparing. Necessary because this impacts indexing of
   * values.
   */
  public boolean isTokenized();
  
  public double compare(String v1, String v2);
  
}