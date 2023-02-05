package repositories.tarea

import models.Tarea
import org.litote.kmongo.Id
import repositories.CrudRepository

 interface TareasRepository:CrudRepository<Tarea, Id<Tarea>> {
}