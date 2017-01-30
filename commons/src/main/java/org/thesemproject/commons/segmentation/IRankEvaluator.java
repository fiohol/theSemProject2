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
package org.thesemproject.commons.segmentation;

import java.io.Serializable;


/**
 *
 * @author The Sem Project
 */
public interface IRankEvaluator extends Serializable {

    /**
     * Chiave per identificare il concetto di classificazione come primo
     * elemento della chiave (il secondo dovrà essere il valore della
     * classificazione)
     */
    public static final String CLASSIFICATIONS = "[Classifications]";

    /**
     * Condizione uguale a
     */
    String EQUALS = "=";
    /**
     * Condizione maggiore di
     */
    String GREAT = ">";
    /**
     * Condizione maggiore o uguale a
     */
    String GREAT_OR_EQUAL = ">=";
    /**
     * Condizione minore di
     */
    String LESS = "<";
    /**
     * Condizione minore o uguale a
     */
    String LESS_OR_EQUAL = "<=";
    /**
     * Condizione match regex
     */
    String MATCH_REGEX = "Match";
    /**
     * Condizione diverso da
     */
    String NOT_EQUALS = "!=";
    
    
    /**
     * Elenco delle condizioni
     */
    String[] CONDITIONS = {GREAT, LESS, EQUALS, NOT_EQUALS, GREAT_OR_EQUAL, LESS_OR_EQUAL, MATCH_REGEX};

    /**
     * Valuta i segmenti in funzione del risultato della segmentazione e delle
     * durate
     *
     * @param identifiedSegments risultato segmentazione
     * @param durations durate globali
     * @return punteggio
     */
    double evaluate(Object identifiedSegments, Object durations);

    /**
     *
     * @return durata
     */
    double getDuration();

    /**
     *
     * @return condizione di durata
     */
    String getDurationCondition();

    /**
     *
     * @return ritorna l'anno di fine
     */
    int getEndYear();

    /**
     *
     * @return ritorna il campo su cui è espressa la condizione
     */
    String getField();

    /**
     *
     * @return ritorna l'operatore
     */
    String getFieldConditionOperator();

    /**
     *
     * @return ritorna il valore della condizione
     */
    String getFieldConditionValue();

    /**
     *
     * @return ritorna il punteggio se le condizioni sono soddisfatte
     */
    double getScore();

    /**
     *
     * @return ritorna l'anno inizio
     */
    int getStartYear();

    /**
     * Imposta la durata
     *
     * @param duration durata
     */
    void setDuration(double duration);

    /**
     * Imposta la condizione di durata
     *
     * @param durationCondition condizione di durata
     */
    void setDurationCondition(String durationCondition);

    /**
     * Imposta l'anno di fine
     *
     * @param endYear anno di fine
     */
    void setEndYear(int endYear);

    /**
     * Imposta il campo su cui si vuole costruire la condizione
     *
     * @param field campo
     */
    void setField(String field);

    /**
     * imposta l'operatore
     *
     * @param fieldConditionOperator operatore
     */
    void setFieldConditionOperator(String fieldConditionOperator);

    /**
     * Imposta il valore della condizione
     *
     * @param fieldConditionValue valore della condizione
     */
    void setFieldConditionValue(String fieldConditionValue);

    /**
     * Imposta il punteggio
     *
     * @param score punteggio
     */
    void setScore(double score);

    /**
     * Imposta l'anno inizio
     *
     * @param startYear anno inizio
     */
    void setStartYear(int startYear);
    
}
