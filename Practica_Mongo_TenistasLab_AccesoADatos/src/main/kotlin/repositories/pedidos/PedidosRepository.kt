package repositories.pedidos

import models.Pedidos
import org.litote.kmongo.Id
import repositories.CrudRepository

interface PedidosRepository:CrudRepository<Pedidos, Id<Pedidos>> {
}