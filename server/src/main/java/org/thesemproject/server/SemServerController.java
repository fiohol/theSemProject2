/*
 * Copyright 2016 The Sem Project.
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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author The Sem Project
 */
@Controller
public class SemServerController {

    /**
     * Percorso di default delle jsp
     */
    public static final String DEFAULT_PAGE = "servers";

    /**
     * Pagina di cambio password
     */
    public static final String DEFAULT_PAGE_PWD = "password";

    /**
     * Pagina di login
     */
    public static final String DEFAULT_PAGE_LOGIN = "login";

    /**
     * Percorso di default
     */
    public static final String DEFAULT_LINK = "/administration";

    /**
     * Nome del bean
     */
    public static final String BEAN_NAME = "SemServer";

    /**
     * Nome del bean password
     */
    public static final String BEAN_NAME_PWD = "SemServerPwd";

    /**
     * Nome del bean login
     */
    public static final String BEAN_NAME_LOGIN = "SemServerLogin";

    /**
     * Parametro che contiene se l'utente è autenticato
     */
    public static final String AUTHENTICATED = "SemAuthenticated";

    /**
     * Pagina base: elenco ambienti e possibilita' di inserirne uno nuovo
     *
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina di default
     */
    @RequestMapping(value = DEFAULT_LINK, method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        standardCase(model, request, response, null);
        return DEFAULT_PAGE;
    }

    /**
     * Pagina di cambio password
     *
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina di default
     */
    @RequestMapping(value = DEFAULT_LINK + "/password", method = RequestMethod.GET)
    public String password(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        model.addAttribute(BEAN_NAME_PWD, new SemServerPwdBean());
        return DEFAULT_PAGE_PWD;
    }

    /**
     * Esegue il cambio password
     *
     * @param pwd bean delle password
     * @param result risultato
     * @param model modello MVC
     * @param request request
     * @param response response
     * @return pagina di cambio password con eventual gestione errore
     */
    @RequestMapping(value = DEFAULT_LINK + "/doPwdChange", method = RequestMethod.POST)
    public String doPwdChange(@Valid @ModelAttribute(BEAN_NAME_PWD) SemServerPwdBean pwd, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        if (!result.hasErrors()) {

            String adminPwd = SemServerConfiguration.getInstance().getAdminPassword();
            if (adminPwd.equals(pwd.old)) {
                if (pwd.new1.equals(pwd.new2)) {
                    SemServerConfiguration.getInstance().updatePassword(pwd.new1);
                    pwd = new SemServerPwdBean();
                    pwd.msg = "Password cambiata correttamente";
                } else {
                    pwd.msg = "Le nuove password non coincidono";
                }
            } else {
                pwd.msg = "Password vecchia errata";
            }
        }
        model.addAttribute(BEAN_NAME_PWD, pwd);
        return DEFAULT_PAGE_PWD;
    }

    /**
     * Presenta la pagina di login
     *
     * @param model modello MVC
     * @param request request
     * @param response response
     * @return pagina di login
     */
    @RequestMapping(DEFAULT_LINK + "/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
        return DEFAULT_PAGE_LOGIN;
    }

    /**
     * Esegue il login se la pwd è corretta
     *
     * @param pwd password
     * @param result risultato del binding
     * @param model modello MVC
     * @param request request
     * @param response response
     * @return pagina di default se ok
     */
    @RequestMapping(DEFAULT_LINK + "/doLogin")
    public String doLogin(@Valid @ModelAttribute(BEAN_NAME_LOGIN) SemServerLoginBean pwd, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!result.hasErrors()) {
            String adminPwd = SemServerConfiguration.getInstance().getAdminPassword();
            if (adminPwd.equals(pwd.getPwd())) {
                request.getSession().setAttribute(AUTHENTICATED, Boolean.TRUE);
                standardCase(model, request, response, null);
                return DEFAULT_PAGE;
            } else {
                pwd.msg = "Password errata";
            }
        }
        model.addAttribute(BEAN_NAME_LOGIN, pwd);
        return DEFAULT_PAGE_LOGIN;
    }

    /**
     * Fa il logout dalla console di amministrazione
     *
     * @param model modello MVC
     * @param request request
     * @param response response
     * @return pagina di login
     */
    @RequestMapping(DEFAULT_LINK + "/doLogout")
    public String doLogout(Model model, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(AUTHENTICATED);
        model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
        return DEFAULT_PAGE_LOGIN;
    }

    /**
     * Controlla se l'utente è autenticato
     *
     * @param request request
     * @return true se autenticato
     */
    private boolean isAuthenticated(HttpServletRequest request) {
        return Boolean.TRUE.equals(request.getSession().getAttribute(AUTHENTICATED));
    }

    /**
     * Caso base: lista ambineti e gestione del bean per inserimento o modifica
     *
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @param sem ambiente (vuoto se per inserimento, compilato se modfica)
     */
    private void standardCase(Model model, HttpServletRequest request, HttpServletResponse response, SemServer sem) {
        getList(model, request, response);
        if (sem == null) {
            sem = new SemServer();
        }
        model.addAttribute(BEAN_NAME, sem);
    }

    /**
     * Lista degli ambienti disponibili
     *
     * @param model modello MVC
     * @param request http request
     * @param response http repsonse
     */
    private void getList(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<SemServer> list = SemServerConfiguration.getInstance().getServers();
        model.addAttribute("list" + BEAN_NAME, list);
    }

    /**
     * Gestisce l'aggiunta o la modifica di un ambiente
     *
     * @param sem bean compilato da JSP
     * @param result risultato del binding
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina base
     */
    @RequestMapping(value = DEFAULT_LINK + "/add", method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute(BEAN_NAME) SemServer sem, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        if (result.hasErrors()) {
            standardCase(model, request, response, sem);
            return DEFAULT_PAGE;
        }
        SemServerConfiguration.getInstance().setServer(sem);
        return SemServerConfiguration.REDIRECT_CONTROLLER + DEFAULT_LINK;
    }

    /**
     * Cancella un ambiente
     *
     * @param name id ambiente
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina di default
     */
    @RequestMapping(DEFAULT_LINK + "/remove/{name}")
    public String remove(@PathVariable("name") final String name, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        SemServerConfiguration.getInstance().removeServer(name);
        return SemServerConfiguration.REDIRECT_CONTROLLER + DEFAULT_LINK;
    }

    /**
     * Carica nel modello un server
     *
     * @param name nome server
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina di default
     */
    @RequestMapping(DEFAULT_LINK + "/edit/{name}")
    public String edit(@Valid @PathVariable("name") String name, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        standardCase(model, request, response, SemServerConfiguration.getInstance().getServer(name));
        return DEFAULT_PAGE;
    }

    /**
     * Inizializza nel modello un server
     *
     * @param name nome server
     * @param model modello MVC
     * @param request http request
     * @param response http response
     * @return pagina di default
     */
    @RequestMapping(DEFAULT_LINK + "/init/{name}")
    public String init(@Valid @PathVariable("name") String name, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!isAuthenticated(request)) {
            model.addAttribute(BEAN_NAME_LOGIN, new SemServerLoginBean());
            return DEFAULT_PAGE_LOGIN;
        }
        standardCase(model, request, response, SemServerConfiguration.getInstance().getServer(name));
        SemServer s = SemServerConfiguration.getInstance().getServer(name);
        if (s != null) {
            s.init();
        }
        return DEFAULT_PAGE;
    }

}
