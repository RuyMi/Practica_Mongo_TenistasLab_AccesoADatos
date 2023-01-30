package controller


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import models.*
import models.enums.TipoPerfil
import mu.KotlinLogging
import org.litote.kmongo.Id
import repositories.maquina.MaquinaRepositoryImpl
import repositories.pedidos.PedidosRepositoryImpl
import repositories.producto.ProductoRepositoryImpl
import repositories.tarea.TareasRepositoryImpl
import repositories.turno.TurnoRepositoryImpl
import repositories.usuario.UsuarioRepositoryImpl
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * Controlador
 *
 * @property MaquinaEncordarRepositoryImpl
 * @property MaquinaPersonalizacionRepositoryImpl
 * @property PedidosRepositoryImpl
 * @property ProductoRepositoryImpl
 * @property TareaRepositoryImpl
 * @property UsuarioRepositoryImpl
 * @property TurnosRepositoryImpl
 * @property usuarioActual
 * @constructor Create empty Controlador
 */
class Controlador(
    val MaquinaEncordarRepositoryImpl: MaquinaRepositoryImpl,
    val MaquinaPersonalizacionRepositoryImpl: MaquinaRepositoryImpl,
    val PedidosRepositoryImpl: PedidosRepositoryImpl,
    val ProductoRepositoryImpl: ProductoRepositoryImpl,
    val TareaRepositoryImpl: TareasRepositoryImpl,
    val UsuarioRepositoryImpl: UsuarioRepositoryImpl,
    val TurnosRepositoryImpl: TurnoRepositoryImpl,
    val usuarioActual: Usuario
) {

    //Maquina

    /**
     * Listar maquinas
     *
     * @return devuelve una lista de Maquina
     */
    fun listarMaquinasPerso(): Flow<Maquina> {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO){
            MaquinaPersonalizacionRepositoryImpl.findAll()
        }else{
            logger.error { "No tienes permiso para buscar máquinas" }
            emptyFlow()
        }

    }

    /**
     * Encontrar maquina id
     *
     * @param id
     * @return  Una Maquina
     */
    suspend fun encontrarMaquinaID(id: Id<Maquina>): Maquina? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            MaquinaPersonalizacionRepositoryImpl.findById(id)
        }else{
            logger.error { "No tienes permiso para buscar una máquina" }
            null
        }
    }

    /**
     * Encontrar maquina uuid
     *
     * @param uuid
     * @return Una MaquinaPersonalizacion
     */
    suspend fun encontrarMaquinaUUID(uuid: UUID): Maquina? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
             MaquinaPersonalizacionRepositoryImpl.findByUUID(uuid)
        }else{
            logger.error { "No tienes permiso para buscar una máquina" }
            null
        }
    }

    /**
     * Guardar maquina
     *
     * @param maquina
     * @return guarda una Maquina
     */
    suspend fun guardarMaquina(maquina: Maquina): Maquina? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            MaquinaPersonalizacionRepositoryImpl.save(maquina)
        }else{
            logger.error { "No tienes permiso para guardar una máquina" }
             null
        }
    }

    /**
     * Borrar maquina
     *
     * @param maquina
     * @return devuelve true si borra la maquina
     */
    suspend fun borrarMaquina(maquina: Maquina): Boolean {
        val temp = listarTareas().filter { !it.estadoCompletado }
        return if(temp.count{ it.maquina?.numSerie == maquina.numSerie} == 0){
            if(usuarioActual.perfil == TipoPerfil.ADMINISTRADOR) {
                MaquinaPersonalizacionRepositoryImpl.delete(maquina)
            }else{
                logger.error { "Solo un administrador puede borrar una máquina" }
                false
            }
        }else{
            logger.error { "No se puede borrar ya que esta máquina tiene asignadas tareas" }
            false
        }
    }

    /**
     * Listar pedidos
     *
     * @return una lista de Pedidos
     *///Pedidos
    fun listarPedidos(): Flow<Pedidos> {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            PedidosRepositoryImpl.findAll()
        }else{
            PedidosRepositoryImpl.findAll().filter{
                it.usuario == usuarioActual.id
            }
        }
    }

    /**
     * Encontrar pedido id
     *
     * @param id
     * @return devuelve un Pedido
     */
    suspend fun encontrarPedidoID(id: Id<Pedidos>): Pedidos? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            PedidosRepositoryImpl.findById(id)
        }else{
            logger.error { "No tienes permiso para encontrar pedidos" }
            null
        }
    }

    /**
     * Encontrar pedido uuid
     *
     * @param uuid
     * @return devuelve un Pedido
     */
    suspend fun encontrarPedidoUUID(uuid: UUID): Pedidos? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            PedidosRepositoryImpl.findByUUID(uuid)
        }else{
            logger.error { "No tienes permiso para encontrar pedidos" }
            null
        }
    }

    /**
     * Guardar pedido
     *
     * @param pedidos
     * @return guarda un Pedidos
     */
    suspend fun guardarPedido(pedidos: Pedidos): Pedidos? {
        return if(usuarioActual.perfil == TipoPerfil.ADMINISTRADOR || usuarioActual.perfil == TipoPerfil.USUARIO){
            PedidosRepositoryImpl.save(pedidos)
        }else{
            logger.debug{"Solo los usuarios o administradores pueden crear o actualizar pedidos"}
            null
        }

    }

    /**
     * Borrar Pedido
     *
     * @param pedidos
     * @return devuelve true si borra un pedido
     */
    suspend fun borrarPedido(pedidos: Pedidos): Boolean {
        return if(usuarioActual.perfil == TipoPerfil.ADMINISTRADOR){
            PedidosRepositoryImpl.delete(pedidos)
        }else{
            logger.debug{"Solo los administradores pueden eliminar pedidos"}
            false
        }

    }

    //Productos

    /**
     * Listar productos
     *
     * @return una lista de productos
     */
    fun listarProductos(): Flow<Producto> {
        return ProductoRepositoryImpl.findAll()
    }

    /**
     * Encontrar producto i d
     *
     * @param id
     * @return devuelve un Producto
     */
    suspend fun encontrarProductoID(id: Id<Producto>): Producto? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            ProductoRepositoryImpl.findById(id)
        }else{
            logger.error { "No tienes permiso para encontrar productos" }
            null
        }
    }

    /**
     * Encontrar producto uuid
     *
     * @param uuid
     * @return devuelve un Producto
     */
    suspend fun encontrarProductoUUID(uuid: UUID): Producto? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            ProductoRepositoryImpl.findByUUID(uuid)
        }else{
            logger.error { "No tienes permiso para encontrar productos" }
            null
        }
    }

    /**
     * Guardar producto
     *
     * @param producto
     * @return guarda un Producto
     */
    suspend fun guardarProducto(producto: Producto): Producto? {
        return if(usuarioActual.perfil == TipoPerfil.ADMINISTRADOR){
            ProductoRepositoryImpl.save(producto)
        }else{
            logger.error { "No tienes permiso para crear o actualizar productos" }
            null
        }
    }

    /**
     * Borrar producto
     *
     * @param producto
     *  @return devuelve un true si borra el producto
     */
    suspend fun borrarProducto(producto: Producto): Boolean {
        return if(usuarioActual.perfil == TipoPerfil.ADMINISTRADOR){
            ProductoRepositoryImpl.delete(producto)
        }else{
            logger.error { "Solo los administradores pueden eliminar productos" }
            false
        }

    }

    /**
     * Listar tareas
     *
     * @return un Flow de Tareas
     *///Tareas
    fun listarTareas(): Flow<Tarea> {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO) {
            TareaRepositoryImpl.findAll()
        }else{
            logger.error { "No tienes permiso para encontrar tareas" }
            emptyFlow()
        }
    }

    /**
     * Encontrar tarea id
     *
     * @param id
     * @return devuelve una Tarea
     */
    suspend fun encontrarTareaID(id: Id<Tarea>): Tarea? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO){
             TareaRepositoryImpl.findById(id)
        } else{
            logger.debug{"No tienes permiso para encontrar tareas"}
            null
        }

    }

    /**
     * Encontrar tarea uuid
     *
     * @param uuid
     *@return devuelve una Tarea
     */
    suspend fun encontrarTareaUUID(uuid: UUID): Tarea? {
        return if(usuarioActual.perfil != TipoPerfil.USUARIO){
            TareaRepositoryImpl.findByUUID(uuid)
        } else{
            logger.debug{"No tienes permiso para encontrar tareas"}
            null
        }
    }

    /**
     * Guardar tarea
     *
     * @param tarea
     * @return guarda una Tarea
     *
     *En caso de que la tarea no este en un turno se podrá añadir ese turno
     * ,si no, no podrá añadirsea otro turno.
     */
    suspend fun guardarTarea(tarea: Tarea): Tarea? {
        val temp = listarTareas()
        val turnoActual = encontrarTurnoUUID(tarea.turno.uuidTurno)
        val empleado = encontrarUsuarioUUID(tarea.empleado.uuidUsuario)
        return if (turnoActual != null && empleado != null) {
            val veces = temp.filter { !it.estadoCompletado }.filter { it.turno.uuidTurno == turnoActual.uuidTurno }.count { it.empleado.uuidUsuario == empleado.uuidUsuario }
            if(veces < 2){
                TareaRepositoryImpl.save(tarea)
            }else{
                logger.debug { "No puede tener 2 tareas en el mismo turno a la vez el empleado con uuid: ${empleado.uuidUsuario}"}
                null
            }
        }else{
            logger.debug { "No existe el empleado: ${empleado!!.uuidUsuario}"}
            null
        }

    }

    /**
     * Borrar tarea
     *
     * @param tarea
     * @return devuelve un true si se borra
     */
    suspend fun borrarTarea(tarea: Tarea): Boolean {
        return TareaRepositoryImpl.delete(tarea)
    }

    /**
     * Listar usuarios
     *
     * @return devuelve una lista de Usuarios
     *///Usuario
    fun listarUsuarios(): List<Usuario> {
        return UsuarioRepositoryImpl.findAll()
    }

    /**
     * Encontrar usuario i d
     *
     * @param id
     * @return devuelve un Usuario
     */
    fun encontrarUsuarioID(id: Int): Usuario? {
        if(id > 0){
            return UsuarioRepositoryImpl.findById(id)
        } else{
            logger.debug{"No puedes encontrar un usuario que tenga un id menor que 1. Id introducido: $id"}
        }
        return null
    }

    /**
     * Encontrar usuario u u i d
     *
     * @param uuid
     * @return devuelve un Usuario
     */
    fun encontrarUsuarioUUID(uuid: UUID): Usuario? {
        return UsuarioRepositoryImpl.findbyUUID(uuid)
    }

    /**
     * Guardar usuario
     *
     * @param usuario
     *@return devuelve el usuario guardado
     */
    fun guardarUsuario(usuario: Usuario): Usuario {
        return UsuarioRepositoryImpl.save(usuario)
    }

    /**
     * Borrar usuario
     *
     * @param usuario
     * @return devuelve true si borra al usuario
     */
    fun borrarUsuario(usuario: Usuario): Boolean {
        return if(usuario.perfil == TipoPerfil.ENCORDADOR){
            val temp = listarTareas().filter { !it.estadoCompletado }.count { it.empleado.uuid == usuario.uuid }
            if (temp == 0){
                UsuarioRepositoryImpl.delete(usuario)
            }else{
                false
            }
        } else{
            UsuarioRepositoryImpl.delete(usuario)
        }
    }

    /**
     * Listar turnos
     *
     *  @return una lista de turnos
     *///Turnos
    fun listarTurnos(): List<Turno> {
        return TurnosRepositoryImpl.findAll()
    }

    /**
     * Encontrar turno id
     *
     * @param id
     * @return  devuelve un turno
     */
    fun encontrarTurnoID(id: Int): Turno? {
        if(id > 0){
            return TurnosRepositoryImpl.findById(id)
        } else{
            logger.debug{"No puedes encontrar un usuario que tenga un id menor que 1. Id introducido: $id"}
        }
        return null
    }

    /**
     * Encontrar turno uuid
     *
     * @param uuid
     * @return devuelve un turno
     */
    fun encontrarTurnoUUID(uuid: UUID): Turno? {
        return TurnosRepositoryImpl.findbyUUID(uuid)
    }

    /**
     * Guardar turno
     *
     * @param turno
     * @return devuelve el turno guardado
     */
    fun guardarTurno(turno: Turno): Turno {
        return TurnosRepositoryImpl.save(turno)
    }

    /**
     * Borrar turno
     *
     * @param turno
     * @return devuelve true si borra el turno
     */
    fun borrarTurno(turno: Turno): Boolean {
        return if (usuarioActual.perfil == TipoPerfil.ADMINISTRADOR) {
            TurnosRepositoryImpl.delete(turno)
        } else {
            logger.debug("Solo un administrador puede eliminar el turno")
            false
        }
    }

}




