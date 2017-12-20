/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.driver;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatementDelayInterceptor extends StatementInterceptor {
    
    private final static Logger log = LoggerFactory.getLogger(StatementDelayInterceptor.class);
    
    private long delay = 10;

    /**
     * @param wrapped
     */
    public StatementDelayInterceptor(Object wrapped) {
        super(wrapped);
        
        
        String delayProperty = StatementDelayInterceptor.class.getName() + ".delay";
        String delayValue = System.getProperty(delayProperty);
        if (delayValue != null) {
            delay = Long.parseLong(delayValue.trim());
        }
    }
    
    public void preparedStatementExecute(String methodName, long elapsed, String sql) {
        if (sql.toLowerCase().startsWith("insert") || sql.toLowerCase().startsWith("update")) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            log.info("PreparedStatement." + methodName + " DELAYED (" + (elapsed+delay) + "ms.) " + sql) ;
        } else {
          //  super.preparedStatementExecute(methodName, elapsed, sql);
        }
                  
    }
    
    public void statementExecute(String methodName, long elapsed, Object... parameters) {
        super.statementExecute(methodName, elapsed, parameters);          
    }

}
