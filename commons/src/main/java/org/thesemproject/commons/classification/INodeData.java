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

import java.util.List;
import org.jdom2.Element;

/**
 *
 * @author The Sem Project
 */
public interface INodeData {

    /**
     * Ritorna la lista dei nomi dei figli del nodo corrente
     *
     * @return Lista dei nomi dei figli
     */
    List<String> getChildrenNames();

    /**
     * Ritorna la lista dei nomi dei figli del nodo corrente
     *
     * @since 1.6
     * @return Lista dei figli
     */
    List<INodeData> getChildrens();

    
    /**
     * Ritorna una label associat al nodo
     *
     * @param language lingua in cui si vuole la label
     * @return label del nodo
     */
    String getLabel(String language);

    /**
     * Ritorna il nome di un nodo dato l'ID
     *
     * @param nid ID del nodo
     * @return nome del nodo
     */
    String getNameFromId(String nid);

    /**
     * Ritorna il figlio con un determinato nuome
     *
     * @param childrenName nome del filglio
     * @return figlio oppure null se non trovato.
     */
    INodeData getNode(String childrenName);

    /**
     * Ritorna il livello da cui inizia a classificare
     *
     * @since 1.1
     * @return livello da cui iniziare a classificare
     */
    int getStartLevel();

    /**
     * Verifica se un nodo è una foglia
     *
     * @return true se non ha figli
     */
    boolean hasChildren();

    /**
     * Imposta una label per il nodo in una particolare lingua
     *
     * @param language lingua
     * @param label etichetta
     */
    void setLabel(String language, String label);

    /**
     * Imposta il livello da cui iniziare a classificare
     *
     * @param startLevel livello da cui iniziare a classificare
     */
    void setStartLevel(int startLevel);

    /**
     * Imposta true se il nodo è istruito
     *
     * @param trained true se istruito
     */
    void setTrained(boolean trained);

    /**
     * Controlla se il percorso esiste
     *
     * @since 1.6
     * @param cp percorso
     * @return true se esiste
     */
    boolean verifyPath(IClassificationPath cp);

    /**
     * Ritorna la visita del sottoalbero a partire dal nodo corrente nel formato
     * confrontabile con ClassificationPath.getCompactClassString();
     *
     * @since 1.3.4
     * @param parentPath percorso per arrivare al nodo
     * @return path del nodo e dei sottorami
     */
    List<String> visitSubTree(String parentPath);

    
    /**
     * Ritorna la vista XML
     * @return XML
     */

    Element getXml();
    
    
}
