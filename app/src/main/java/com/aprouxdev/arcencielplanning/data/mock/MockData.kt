package com.aprouxdev.arcencielplanning.data.mock

import com.aprouxdev.arcencielplanning.data.enums.AlertType
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Comment
import com.aprouxdev.arcencielplanning.data.models.Event
import java.time.Instant
import java.time.LocalDate
import java.util.*


class MockData {
    companion object {
        val alert1 = Alert(id = "1", type = AlertType.General.name, title = "Information Noël", body = "La braderie de noël aura lieu à l'arande le 9 décambre prochain ! \n N'oubliez pas de prévenir vos proches pour nous donner un coup de main le vendredi soir.", endDate = Date(Date.parse("2023/03/14")))
        val alert2 = Alert(id = "2", type = AlertType.Urgency.name, title = "Attention urgent", body = "Il manque trois bénévoles pour le samedi 12 Novembre. \nCliquez ici pour vous inscrire", endDate = Date(Date.parse("2023/02/12")))
        val alert3 = Alert(id = "3", type = AlertType.Team.name, title = "Tri des jouets", body = "Salut l'équipe, pour information des cartons de puzzle sont à vérifier dans le local", endDate = Date(Date.parse("2023/03/23")))
        val alert4 = Alert(id = "4", type = AlertType.Shop.name, title = "Vacances de la Toussaint", body = "Le magasin sera fermé les samedis 23 Octobre et 2 Novembre", endDate = Date(Date.parse("2023/04/01")))
        val alert5 = Alert(id = "5", type = AlertType.Assiduity.name, title = "On ne te vois plus !?", body = "On espère que tu vas bien ? \n Petit rappel : il est demandé de faire au moins un samedi par mois en tant que bénévole au magasin", endDate = Date(Date.parse("2023/04/04")))


        val comment1 = Comment(id="1", userId = "1", user = "Bob", text = "Un texte de commentaire", date = null)
        val comment2 = Comment(id="2", userId = "2", user = "Isaac", text = "Un texte de com", date = null)
        val comment3 = Comment(id="3", userId = "3", user = "Marie-Françoise", text = "Un texte de comment", date = null)
        val comment4 = Comment(id="4", userId = "4", user = "Angel", text = "Un texte de commentaire commentaire", date = null)
        val comment5 = Comment(id="5", userId = "5", user = "Mike", text = "Un texte de", date = null)
        val comment6 = Comment(id="6", userId = "6", user = "Renée", text = "Un texte de", date = null)




        val event1 = Event(id = "1", date= null, time="08:00", team = Teams.Toys.getName(), users= emptyList(), title = "Tri des jouets", description = "Lorem Ipsum", comments = listOf(
            comment1.toString(), comment2.toString()))
        val event2 = Event(id = "2", date= null, time="10:00", team = Teams.Toys.getName(), users= emptyList(), title = "Tri des jouets", description = "Lorem Ipsum", comments = listOf(
            comment1.toString(), comment2.toString(), comment3.toString(), comment4.toString()))
        val event3 = Event(id = "3", date= null, time="08:00", team = Teams.Other.getName(), users= emptyList(), title = "Nettoyage d'été", description = "Lorem Ipsum", comments = emptyList())
        val event4 = Event(id = "4", date= null, time="08:00", team = Teams.Clothes.getName(), users= emptyList(), title = "Tri vêtement", description = "Lorem Ipsum", comments = listOf(
            comment1.toString(), comment2.toString()))
        val event5 = Event(id = "5", date= null, time="10:00", team = Teams.Cleaning.getName(), users= emptyList(), title = "Ménage", description = "Lorem Ipsum", comments = emptyList())
        val event6 = Event(id = "6", date= null, time="14:00", team = Teams.Shop.getName(), users= emptyList(), title = "Magasin samedi matin", description = "Lorem Ipsum", comments = listOf(
            comment5.toString(), comment6.toString()))
        val event7 = Event(id = "7", date= null, time="12:30", team = Teams.Braderie.getName(), users= emptyList(), title = "Préparation braderie", description = "Lorem Ipsum", comments = emptyList())
        val event8 = Event(id = "8", date= null, time="08:00", team = Teams.Toys.getName(), users= emptyList(), title = "Point jouets", description = "Lorem Ipsum", comments = emptyList())




    }
}