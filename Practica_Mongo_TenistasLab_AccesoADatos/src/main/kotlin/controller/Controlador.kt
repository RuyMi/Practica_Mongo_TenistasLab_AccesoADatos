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
import repositories.usuario.RemoteCachedRepositoryUsuario
import repositories.usuario.UsuarioRepositoryImpl
import repositories.usuario.UsuarioRepositoryKtorfit
import usuarioActual
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * Controlador
 *
 * @property maquinaRepositoryImpl
 * @property pedidosRepositoryImpl
 * @property productoRepositoryImpl
 * @property tareasRepositoryImpl
 * @property usuarioRepositoryImpl
 * @property turnoRepositoryImpl
 * @property usuarioActual
 */
class Controlador(
    private val maquinaRepositoryImpl: MaquinaRepositoryImpl,
    private val pedidosRepositoryImpl: PedidosRepositoryImpl,
    private val productoRepositoryImpl: ProductoRepositoryImpl,
    private val tareasRepositoryImpl: TareasRepositoryImpl,
    private val usuarioRepositoryImpl: UsuarioRepositoryImpl,
    private val turnoRepositoryImpl: TurnoRepositoryImpl,
    private val ktorFitUsuario: UsuarioRepositoryKtorfit,
    private val cacheRepositoryImpl: RemoteCachedRepositoryUsuario,
) {

    //Maquina

    /**
     * Listar maquinas
     *
     * @return devuelve una lista de Maquina
     */
    fun listarMaquinas(): Flow<Maquina> {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO){
            maquinaRepositoryImpl.findAll()
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            maquinaRepositoryImpl.findById(id)
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
    suspend fun encontrarMaquinaUUID(uuid: String): Maquina? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
             maquinaRepositoryImpl.findByUUID(uuid)
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            maquinaRepositoryImpl.save(maquina)
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
            if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR) {
                maquinaRepositoryImpl.delete(maquina)
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            pedidosRepositoryImpl.findAll()
        }else{
            pedidosRepositoryImpl.findAll().filter{
                it.usuario == usuarioActual!!.id
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            pedidosRepositoryImpl.findById(id)
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
    suspend fun encontrarPedidoUUID(uuid: String): Pedidos? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            pedidosRepositoryImpl.findByUUID(uuid)
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
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR || usuarioActual!!.perfil == TipoPerfil.USUARIO){
            pedidosRepositoryImpl.save(pedidos)
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
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            pedidosRepositoryImpl.delete(pedidos)
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
        return productoRepositoryImpl.findAll()
    }

    /**
     * Encontrar producto i d
     *
     * @param id
     * @return devuelve un Producto
     */
    suspend fun encontrarProductoID(id: Id<Producto>): Producto? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            productoRepositoryImpl.findById(id)
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
    suspend fun encontrarProductoUUID(uuid: String): Producto? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            productoRepositoryImpl.findByUUID(uuid)
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
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            productoRepositoryImpl.save(producto)
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
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            productoRepositoryImpl.delete(producto)
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            tareasRepositoryImpl.findAll()
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO){
             tareasRepositoryImpl.findById(id)
        } else{
            logger.error{"No tienes permiso para encontrar tareas"}
            null
        }

    }

    /**
     * Encontrar tarea uuid
     *
     * @param uuid
     *@return devuelve una Tarea
     */
    suspend fun encontrarTareaUUID(uuid: String): Tarea? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO){
            tareasRepositoryImpl.findByUUID(uuid)
        } else{
            logger.error{"No tienes permiso para encontrar tareas"}
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            if (turnoActual != null && empleado != null) {
                val veces = temp
                    .filter { !it.estadoCompletado }
                    .filter { it.turno.uuidTurno == turnoActual.uuidTurno }
                    .count { it.empleado.uuidUsuario == empleado.uuidUsuario }
                if (veces < 2) {
                    tareasRepositoryImpl.save(tarea)
                } else {
                    logger.error { "No puede tener 2 tareas en el mismo turno a la vez el empleado con uuid: ${empleado.uuidUsuario}" }
                    null
                }
            } else {
                logger.error { "No existe el empleado: ${empleado!!.uuidUsuario}" }
                null
            }
        }else{
            logger.error{"No tienes permiso para guardar tareas"}
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
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO){
            tareasRepositoryImpl.delete(tarea)
        } else{
            logger.error{"No tienes permiso para encontrar tareas"}
            false
        }
    }

    //Usuario

    /**
     * Listar usuarios
     *
     * @return devuelve una lista de Usuarios
     */
    fun listarUsuarios(): Flow<Usuario> {
        return if(usuarioActual!!.perfil== TipoPerfil.ADMINISTRADOR){
            usuarioRepositoryImpl.findAll()
        } else{
            logger.error{"Solo un administrador puede listar usuarios"}
            emptyFlow()
        }
    }

    /**
     * Encontrar usuario id
     *
     * @param id
     * @return devuelve un Usuario
     */
    suspend fun encontrarUsuarioID(id: Id<Usuario>): Usuario? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            val res = cacheRepositoryImpl.findById(id.toString().toLong())?.let {
                return it
            }
            return usuarioRepositoryImpl.findById(id)

        } else{
            logger.error{"Solo un administrador puede encontrar usuarios"}
            null
        }
    }

    /**
     * Encontrar usuario uuid
     *
     * @param uuid
     * @return devuelve un Usuario
     */
    suspend fun encontrarUsuarioUUID(uuid: String): Usuario? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            usuarioRepositoryImpl.findByUUID(uuid)
        } else{
            logger.error{"Solo un administrador puede encontrar usuarios"}
            null
        }
    }

    /**
     * Guardar usuario
     *
     * @param usuario
     *@return devuelve el usuario guardado
     */
    suspend fun guardarUsuario(usuario: Usuario): Usuario? {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO){
            usuarioRepositoryImpl.save(usuario)
        } else{
            logger.error{"No tienes permisos para guardar usuarios"}
            null
        }
    }

    /**
     * Borrar usuario
     *
     * @param usuario
     * @return devuelve true si borra al usuario
     */
    suspend fun borrarUsuario(usuario: Usuario): Boolean {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR) {
            if (usuario.perfil == TipoPerfil.ENCORDADOR) {
                val temp = listarTareas().filter { !it.estadoCompletado }
                    .count { it.empleado.uuidUsuario == usuario.uuidUsuario }
                if (temp == 0) {
                    usuarioRepositoryImpl.delete(usuario)
                } else {
                    false
                }
            } else {
                usuarioRepositoryImpl.delete(usuario)
            }
        }else{
            logger.error{"No tienes permisos para borrar usuarios"}
            false
        }
    }

    /**
     * Listar turnos
     *
     *  @return una lista de turnos
     *///Turnos
    fun listarTurnos(): Flow<Turno> {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            turnoRepositoryImpl.findAll()
        } else{
            logger.error{"Solo un administrador puede listar turnos"}
            emptyFlow()
        }
    }

    /**
     * Encontrar turno id
     *
     * @param id
     * @return  devuelve un turno
     */
    suspend fun encontrarTurnoID(id: Id<Turno>): Turno? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            turnoRepositoryImpl.findById(id)
        } else{
            logger.debug{"Solo un administrador puede encontrar turnos"}
            null
        }

    }

    /**
     * Encontrar turno uuid
     *
     * @param uuid
     * @return devuelve un turno
     */
    suspend fun encontrarTurnoUUID(uuid: String): Turno? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            turnoRepositoryImpl.findByUUID(uuid)
        } else{
            logger.debug{"Solo un administrador puede encontrar turnos"}
            null
        }
    }

    /**
     * Guardar turno
     *
     * @param turno
     * @return devuelve el turno guardado
     */
    suspend fun guardarTurno(turno: Turno): Turno? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            turnoRepositoryImpl.save(turno)
        } else{
            logger.debug{"Solo un administrador puede crear o actualizar turnos"}
            null
        }

    }

    /**
     * Borrar turno
     *
     * @param turno
     * @return devuelve true si borra el turno
     */
    suspend fun borrarTurno(turno: Turno): Boolean {
        return if (usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR) {
            turnoRepositoryImpl.delete(turno)
        } else {
            logger.debug("Solo un administrador puede eliminar el turno")
            false
        }
    }

    suspend fun encontrarUsuariosAPI(): Flow<Usuario> {
        return ktorFitUsuario.findAll()
    }

    suspend fun guardarUsuariosAPI(user: Usuario): Usuario {
        return ktorFitUsuario.save(user)
    }

    suspend fun añadirCacheUsuarios(user: Usuario): Usuario {
        return cacheRepositoryImpl.save(user)
    }

}




