package repositories.maquina

import models.Maquina
import org.litote.kmongo.Id
import repositories.CrudRepository

interface MaquinaRepository:CrudRepository<Maquina, Id<Maquina>> {

}