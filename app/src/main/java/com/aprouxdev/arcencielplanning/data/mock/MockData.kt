package com.aprouxdev.arcencielplanning.data.mock

import com.aprouxdev.arcencielplanning.data.enums.AlertType
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Alert
import com.aprouxdev.arcencielplanning.data.models.Event


class MockData {
    companion object {
        val alert1 = Alert(id = "1", type = AlertType.General, title = "Information Noël", body = "La braderie de noël aura lieu à l'arande le 9 décambre prochain ! \n N'oubliez pas de prévenir vos proches pour nous donner un coup de main le vendredi soir.", endDate = "01012023")
        val alert2 = Alert(id = "2", type = AlertType.Urgency, title = "Attention urgent", body = "Il manque trois bénévoles pour le samedi 12 Novembre. \nCliquez ici pour vous inscrire", endDate = "01012023")
        val alert3 = Alert(id = "3", type = AlertType.Team, title = "Tri des jouets", body = "Salut l'équipe, pour information des cartons de puzzle sont à vérifier dans le local", endDate = "01012023")
        val alert4 = Alert(id = "4", type = AlertType.Shop, title = "Vacances de la Toussaint", body = "Le magasin sera fermé les samedis 23 Octobre et 2 Novembre", endDate = "01012023")
        val alert5 = Alert(id = "5", type = AlertType.Assiduity, title = "On ne te vois plus !?", body = "On espère que tu vas bien ? \n Petit rappel : il est demandé de faire au moins un samedi par mois en tant que bénévole au magasin", endDate = "01012023")



        val event1 = Event(id = "1", date= null, time="08:00", team = Teams.Toys, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event2 = Event(id = "2", date= null, time="10:00", team = Teams.Toys, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event3 = Event(id = "3", date= null, time="08:00", team = Teams.Other, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event4 = Event(id = "4", date= null, time="08:00", team = Teams.Clothes, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event5 = Event(id = "5", date= null, time="10:00", team = Teams.Cleaning, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event6 = Event(id = "6", date= null, time="14:00", team = Teams.Shop, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event7 = Event(id = "7", date= null, time="12:30", team = Teams.Braderie, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
        val event8 = Event(id = "8", date= null, time="08:00", team = Teams.Toys, users= emptyList(), title = "Tri au magasin", description = "Lorem Ipsum", comments = emptyList())
    }
}