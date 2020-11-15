/**
 * SYM : Labo 2 - Protocoles applicatifs
 * Auteurs : Julien Béguin, Robin Cuénoud & Gaëtan Daubresse
 * Date : 15.11.2020
 * Classe : B
 */

package ch.heigvd.iict.sym.labo2

interface CommunicationEventListener {
    fun handleServerResponse(response :String)
}