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
package org.thesemproject.commons.classification;

import java.io.Serializable;

/**
 *
 * @author The Sem Project
 */
public interface IClassificationPath extends Serializable {

    /**
     * Aggiunge un risultato di classificazione
     *
     * @param nodeName Nome del nodo (categoria) su cui l'oggetto è classificato
     * @param score valore associato alla classificazione
     * @param level livello di classificazione (da 5 a 0). O è il livello base 5
     * è la foglia
     */
    void addResult(String nodeName, double score, int level);

    /**
     * Ritorna la categoria più profonda di classificazione (Ultimo livello
     * popolato)
     *
     * @return foglia su cui è classificato l'item
     */
    String getLeaf();

    /**
     * Ritorna il nome del nodo ad un determinato livello
     *
     * @param level livello del nodo
     * @return nome del nodo
     */
    String getNodeName(int level);

    /**
     * Ritorna lo score di classificazione al livello prescelto
     *
     * @param level livello
     * @return score
     */
    double getNodeScore(int level);

    /**
     * Ritorna il path di classificazione. IL path si legge da sinistra verso
     * destra. A sinistra il livello più alto nell'albero (indice 0) a destra la
     * foglia. Una classificazione
     *
     * PIPPO &gt; PLUTO &gt; TOPOLINO &gt; MINNIE
     *
     * viene memorizzata come
     *
     * {"PIPPO", "PLUTO", "TOPOLINO", "MINNIE"}
     *
     * @return path
     */
    String[] getPath();

    /**
     * Ritorna gli score come array di double. Il path si legge da sinistra
     * verso destra. A sinistra i livello più alto (indice 0) a destra la foglia
     *
     * @return array degli score
     */
    double[] getScore();

    /**
     * Ritorna la tecnologia con cui è stata fatta la classificazione
     *
     * @return tecnologia utilizzata
     */
    String getTechnology();

    /**
     *
     * @param technology
     */
    void setTechnology(String technology);

    /**
     * Ritorna la rappresentazione della classificazione senza mostrare
     * tecnologia utilizzata e score per ogni livello
     *
     * @return rappresentazione stringa della classificazione
     */
    String toSmallClassString();

    /**
     * Ritorna la rappresentazione stringa della classificazione senza mostrare
     * la tecnologia utilizzata
     *
     * @return rappresentazione stringa della classificazione
     */
    String toSmallString();

    /**
     * Ritorna la rappresentazione stringa del percorso di classificazione
     * Questa rappresentazione visualizza la tecnologia utilizzata, i nodi e il
     * relativo punteggio per ogni nodo
     *
     * @return rappresentazione stringa della classificazione
     */
    String toString();
    
}
