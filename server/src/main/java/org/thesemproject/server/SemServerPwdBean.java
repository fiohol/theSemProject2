/*
 * Copyright 2017 The Sem Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thesemproject.server;

import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Bean per la gestione del cambio password
 *
 * @author The Sem Project
 */
public class SemServerPwdBean {

    @Size(min = 8, max = 10)
    String old = "";

    @Id
    @Size(min = 8, max = 10)
    String new1 = "";

    @Size(min = 8, max = 10)
    String new2 = "";

    String msg = "";

    /**
     * Ritorna la vecchia password
     *
     * @return vecchia password
     */
    public String getOld() {
        return old;
    }

    /**
     * Imposta la vecchia password
     *
     * @param old vecchia password
     */
    public void setOld(String old) {
        this.old = old;
    }

    /**
     * Ritorna la nuova password
     *
     * @return nuova password
     */
    public String getNew1() {
        return new1;
    }

    /**
     * Imposta la nuova password
     *
     * @param new1 nuova password
     */
    public void setNew1(String new1) {
        this.new1 = new1;
    }

    /**
     * Ritorna la nuova password ripetuta
     *
     * @return password ripetuta
     */
    public String getNew2() {
        return new2;
    }

    /**
     * Imposta la password ripetuta
     *
     * @param new2 password ripetuta
     */
    public void setNew2(String new2) {
        this.new2 = new2;
    }

    /**
     * Ritorna il messaggio
     *
     * @return messaggio
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Imposta il messaggio
     *
     * @param msg messaggio
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
