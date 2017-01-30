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
package org.thesemproject.engine.segmentation.functions.rank;

import org.thesemproject.commons.segmentation.IRankEvaluator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.thesemproject.commons.utils.DateUtils;
import org.thesemproject.commons.classification.ClassificationPath;
import org.thesemproject.commons.utils.LogGui;
import org.thesemproject.engine.segmentation.CaptureConfiguration;
import org.thesemproject.engine.segmentation.SegmentConfiguration;
import org.thesemproject.engine.segmentation.SegmentationResults;
import org.thesemproject.engine.segmentation.functions.DurationsMap;


/**
 * Costituisce un elemento di valutazione di un documento segmentato
 *
 * @since 1.3.4
 * @author The Sem Project
 */
public class RankEvaluator implements IRankEvaluator {

    private String field;
    private String fieldConditionOperator;
    private String fieldConditionValue;
    private int startYear;
    private int endYear;
    private double duration;
    private String durationCondition;
    private double score;

    /**
     * Costruttore
     *
     * @param field campo su cui si vuole costruire la condizione
     * @param fieldConditionOperator operatore
     * @param fieldConditionValue valore
     * @param score punteggio
     */
    public RankEvaluator(String field, String fieldConditionOperator, String fieldConditionValue, double score) {
        this.field = field;
        this.fieldConditionOperator = fieldConditionOperator;
        this.fieldConditionValue = fieldConditionValue;
        this.score = score;
        this.startYear = 0;
        this.endYear = 0;
        this.durationCondition = null;
    }

    /**
     * Costruttore
     *
     * @param field campo su cui si vuole costruire la condizione
     * @param fieldConditionOperator operatore
     * @param fieldConditionValue valore
     * @param startPeriod anno inizio
     * @param endPeriod anno fine
     * @param score punteggio
     */
    public RankEvaluator(String field, String fieldConditionOperator, String fieldConditionValue, int startPeriod, int endPeriod, double score) {
        this.field = field;
        this.fieldConditionOperator = fieldConditionOperator;
        this.fieldConditionValue = fieldConditionValue;
        this.startYear = startPeriod;
        this.endYear = endPeriod;
        this.score = score;
        this.durationCondition = null;
    }

    /**
     * Costruttore
     *
     * @param field campo su cui si vuole costruire la condizione
     * @param fieldConditionOperator operatore
     * @param fieldConditionValue valore
     * @param duration durata
     * @param durationCondition operatore di durata
     * @param score punteggio
     */
    public RankEvaluator(String field, String fieldConditionOperator, String fieldConditionValue, double duration, String durationCondition, double score) {
        this.field = field;
        this.fieldConditionOperator = fieldConditionOperator;
        this.fieldConditionValue = fieldConditionValue;
        this.duration = duration;
        this.durationCondition = durationCondition;
        if (this.durationCondition != null) {
            if (this.durationCondition.length() == 0)
                this.durationCondition = null;
        }
        this.score = score;
        this.startYear = 0;
        this.endYear = 0;
    }

    /**
     * Costruttore
     *
     * @param field campo su cui si vuole costruire la condizione
     * @param fieldConditionOperator operatore
     * @param fieldConditionValue valore
     * @param startPeriod periodo inizio
     * @param endPeriod periodo fine
     * @param duration durata
     * @param durationCondition condizione di durata
     * @param score punteggio
     */
    public RankEvaluator(String field, String fieldConditionOperator, String fieldConditionValue, int startPeriod, int endPeriod, double duration, String durationCondition, double score) {
        this.field = field;
        this.fieldConditionOperator = fieldConditionOperator;
        this.fieldConditionValue = fieldConditionValue;
        this.startYear = startPeriod;
        this.endYear = endPeriod;
        this.duration = duration;
        this.durationCondition = durationCondition;
        if (this.durationCondition != null) {
            if (this.durationCondition.length() == 0)
                this.durationCondition = null;
        }
        this.score = score;
    }

    /**
     *
     * @return ritorna il campo su cui è espressa la condizione
     */
    @Override
    public String getField() {
        return field;
    }

    /**
     * Imposta il campo su cui si vuole costruire la condizione
     *
     * @param field campo
     */
    @Override
    public void setField(String field) {
        this.field = field;
    }

    /**
     *
     * @return ritorna l'operatore
     */
    @Override
    public String getFieldConditionOperator() {
        return fieldConditionOperator;
    }

    /**
     * imposta l'operatore
     *
     * @param fieldConditionOperator operatore
     */
    @Override
    public void setFieldConditionOperator(String fieldConditionOperator) {
        this.fieldConditionOperator = fieldConditionOperator;
    }

    /**
     *
     * @return ritorna il valore della condizione
     */
    @Override
    public String getFieldConditionValue() {
        return fieldConditionValue;
    }

    /**
     * Imposta il valore della condizione
     *
     * @param fieldConditionValue valore della condizione
     */
    @Override
    public void setFieldConditionValue(String fieldConditionValue) {
        this.fieldConditionValue = fieldConditionValue;
    }

    /**
     *
     * @return durata
     */
    @Override
    public double getDuration() {
        return duration;
    }

    /**
     * Imposta la durata
     *
     * @param duration durata
     */
    @Override
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     *
     * @return condizione di durata
     */
    @Override
    public String getDurationCondition() {
        return durationCondition;
    }

    /**
     * Imposta la condizione di durata
     *
     * @param durationCondition condizione di durata
     */
    @Override
    public void setDurationCondition(String durationCondition) {
        this.durationCondition = durationCondition;
    }

    /**
     *
     * @return ritorna il punteggio se le condizioni sono soddisfatte
     */
    @Override
    public double getScore() {
        return score;
    }

    /**
     * Imposta il punteggio
     *
     * @param score punteggio
     */
    @Override
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Valuta i segmenti in funzione del risultato della segmentazione e delle
     * durate
     *
     * @param o1 risultato segmentazione
     * @param o2 durate globali
     * @return punteggio
     */
    @Override
    public double evaluate(Object o1, Object o2) {
        Map<SegmentConfiguration, List<SegmentationResults>> identifiedSegments = (Map<SegmentConfiguration, List<SegmentationResults>>) o1;
        DurationsMap durations = (DurationsMap) o2;
        if ((durationCondition == null)&& startYear == 0 && endYear == 0) {
            return evaluate(identifiedSegments, "", durations);
        } else {
            return evaluate(durations);
        }
    }

    private double evaluate(Map<SegmentConfiguration, List<SegmentationResults>> identifiedSegments, String parentName, DurationsMap durations) {
        double returnScore = 0;
        Set<SegmentConfiguration> segments = identifiedSegments.keySet();
        for (SegmentConfiguration s : segments) {
            String segmentName = (parentName.length() > 0 ? parentName + "." : "") + s.getName();
            Map<String, Integer> cellIndex = null;
            List<SegmentationResults> srs = identifiedSegments.get(s);
            for (SegmentationResults sr : srs) {
                Map<SegmentConfiguration, List<SegmentationResults>> subSegments = sr.getSubsentencies();
                if (subSegments.size() > 0) {// HO sottosegmenti
                    returnScore += evaluate(subSegments, segmentName, durations);
                } else {
                    returnScore += evaluate(sr);
                }
            }
        }
        return returnScore;
    }

    private double evaluate(SegmentationResults sr) { //Valutazioni a livello segmento
        //Valuta il rank sul segmento ricevuto
        if (IRankEvaluator.CLASSIFICATIONS.equals(this.field)) { //Si tratta di valutare la classificazione
            if (EQUALS.equals(this.fieldConditionOperator)) { //Devo valuatare se è una classificazione è uguale
                List<ClassificationPath> cpl = sr.getClassificationPaths();
                for (ClassificationPath cp : cpl) {
                    String path = cp.toSmallClassString();
                    if (path.equals(this.fieldConditionValue)) {
                        return score;
                    }
                }
            }
        } else {
            CaptureConfiguration cc = null;
            for (CaptureConfiguration srcc : sr.getCaptureConfigurationResults().keySet()) {
                if (srcc.getName().equals(field)) {
                    cc = srcc;
                }
            }
            if (cc != null) { //Il field è catturato
                String value = sr.getCaptureConfigurationResults().get(cc);
                if (value != null) {
                    switch (cc.getType()) {
                        case "date":
                            Date dValue = DateUtils.parseDate(value);
                            Date dField = DateUtils.parseDate(fieldConditionValue);
                            if (dValue != null && dField != null) {
                                if (null != this.fieldConditionOperator) {
                                    switch (this.fieldConditionOperator) {
                                        case GREAT:
                                            if (dValue.after(dField)) {
                                                return score;
                                            }
                                            break;
                                        case EQUALS:
                                            if (dValue.equals(dField)) {
                                                return score;
                                            }
                                            break;
                                        case LESS:
                                            if (dValue.before(dField)) {
                                                return score;
                                            }
                                            break;
                                        case NOT_EQUALS:
                                            if (!dValue.equals(dField)) {
                                                return score;
                                            }
                                            break;
                                        case GREAT_OR_EQUAL:
                                            if (dValue.after(dField)) {
                                                return score;
                                            }
                                            if (dValue.equals(dField)) {
                                                return score;
                                            }
                                            break;
                                        case LESS_OR_EQUAL:
                                            if (dValue.before(dField)) {
                                                return score;
                                            }
                                            if (dValue.equals(dField)) {
                                                return score;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            break;
                        case "integer":
                        case "real":
                        case "number":
                            try {
                                double ddValue = Double.parseDouble(value);
                                double ddField = Double.parseDouble(fieldConditionValue);
                                if (null != this.fieldConditionOperator) {
                                    switch (this.fieldConditionOperator) {
                                        case GREAT:
                                            if (ddValue > ddField) {
                                                return score;
                                            }
                                            break;
                                        case EQUALS:
                                            if (ddValue == ddField) {
                                                return score;
                                            }
                                            break;
                                        case LESS:
                                            if (ddValue <= ddField) {
                                                return score;
                                            }
                                            break;
                                        case NOT_EQUALS:
                                            if (ddValue != ddField) {
                                                return score;
                                            }
                                            break;
                                        case GREAT_OR_EQUAL:
                                            if (ddValue >= ddField) {
                                                return score;
                                            }
                                            break;
                                        case LESS_OR_EQUAL:
                                            if (ddValue <= ddField) {
                                                return score;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                LogGui.printException(e);
                            }
                            break;
                        case "boolean":
                        case "text":
                            if (null != this.fieldConditionOperator) {
                                switch (this.fieldConditionOperator) {
                                    case EQUALS:
                                        if (value.equals(fieldConditionValue)) {
                                            return score;
                                        }
                                        break;
                                    case NOT_EQUALS:
                                        if (!value.equals(fieldConditionValue)) {
                                            return score;

                                        }
                                        break;
                                    case MATCH_REGEX:
                                        if (value.matches(fieldConditionValue)) {
                                            return score;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return 0;
    }

    private double evaluate(DurationsMap durations) { //Valutazione sui dati di durata
        List<Pair<String, String>> pairs = durations.keySet();
        double sum = 0;
        for (Pair<String, String> p : pairs) {
            if (field.equals(p.getLeft())) { //Il left è quello che serve
                String value = p.getRight();
                if ((EQUALS.equals(this.fieldConditionOperator) && value.equals(fieldConditionValue))
                        || (MATCH_REGEX.equals(this.fieldConditionOperator) && value.matches(fieldConditionValue))
                        || (IRankEvaluator.CLASSIFICATIONS.equals(this.field) && fieldConditionValue.endsWith(value))) {
                    double points = 0;
                    if (durationCondition != null) {
                        double dValue = durations.getDurationYears(p);
                        if (null != this.durationCondition) {
                            switch (this.durationCondition) {
                                case GREAT:
                                    if (dValue > duration) {
                                        points += score;
                                    }
                                    break;
                                case EQUALS:
                                    if (dValue == duration) {
                                        points += score;
                                    }
                                    break;
                                case LESS:
                                    if (dValue < duration) {
                                        points += score;
                                    }
                                    break;
                                case NOT_EQUALS:
                                    if (dValue != duration) {
                                        points += score;
                                    }
                                    break;
                                case GREAT_OR_EQUAL:
                                    if (dValue >= duration) {
                                        points += score;
                                    }
                                    break;
                                case LESS_OR_EQUAL:
                                    if (dValue <= duration) {
                                        points += score;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (startYear != 0 && endYear != 0) {
                        List<String> years = new ArrayList(durations.getYears(p));
                        if (years.size() > 0) {
                            Collections.sort(years);
                            int sY = Integer.parseInt(years.get(0));
                            int eY = Integer.parseInt(years.get(years.size() - 1));
                            if (sY >= startYear && eY <= endYear) {
                                points += score;
                            }
                        }
                    } else if (startYear != 0) {
                        List<String> years = new ArrayList(durations.getYears(p));
                        if (years.size() > 0) {
                            Collections.sort(years);
                            int sY = Integer.parseInt(years.get(0));
                            if (sY >= startYear) {
                                points += score;
                            }
                        }
                    }
                    sum += points;
                }
            }
        }
        return sum;
    }

    /**
     *
     * @return ritorna l'anno inizio
     */
    @Override
    public int getStartYear() {
        return startYear;
    }

    /**
     * Imposta l'anno inizio
     *
     * @param startYear anno inizio
     */
    @Override
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     *
     * @return ritorna l'anno di fine
     */
    @Override
    public int getEndYear() {
        return endYear;
    }

    /**
     * Imposta l'anno di fine
     *
     * @param endYear anno di fine
     */
    @Override
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    /**
     * Valuta tutti i valutatori su un risultato di segmentazione
     *
     * @param evaluators lista dei valutatori
     * @param identifiedSegments risultato segmentazione
     * @return punteggio
     */
    public static double evaluate(List<RankEvaluator> evaluators, Map<SegmentConfiguration, List<SegmentationResults>> identifiedSegments) {
        double ret = 0;
        DurationsMap durations = DurationsMap.getDurations(identifiedSegments);
        ret = evaluators.stream().map((re) -> re.evaluate(identifiedSegments, durations)).reduce(ret, (accumulator, _item) -> accumulator + _item);
        return ret;
    }

}
