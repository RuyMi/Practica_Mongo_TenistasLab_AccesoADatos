package repositories.turno

import models.Turno
import org.litote.kmongo.Id
import repositories.CrudRepository

interface TurnoRepository:CrudRepository<Turno, Id<Turno>> {
}