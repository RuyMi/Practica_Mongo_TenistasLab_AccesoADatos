package controller


import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import models.*
import models.enums.TipoPerfil
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.Id
import repositories.maquina.MaquinaRepository
import repositories.pedidos.PedidosRepository
import repositories.producto.ProductoRepository
import repositories.tarea.TareasRepository
import repositories.turno.TurnoRepository
import repositories.usuario.RemoteCachedRepositoryUsuario
import repositories.usuario.UsuarioRepository
import repositories.usuario.UsuarioRepositoryKtorfit
import services.usuarios.UsuariosService

private val logger = KotlinLogging.logger {}

/**
 *
 * Controlador de la aplicación de TenistasLab
 *
 * @property maquinaRepositoryImpl Repositorio de Maquinas
 * @property pedidosRepositoryImpl Repositorio de Pedidos
 * @property productoRepositoryImpl Repositorio de Productos
 * @property tareasRepositoryImpl Repositorio de Tareas
 * @property usuarioRepositoryImpl Repositorio de Usuarios
 * @property turnoRepositoryImpl Repositorio de turnos
 * @property ktorFitUsuario Repositorio de Ktorfit
 * @property cacheRepositoryImpl Repositorio de caché
 * @property usuarioService Servicio de Usuarios
 * @property usuarioActual Usuario actual del programa
 */
@Single
@Named("ControladorTenistas")
class Controlador(
    @Named("MaquinaRepositoryImpl") private val maquinaRepositoryImpl: MaquinaRepository,
    @Named("PedidosRepositoryImpl") private val pedidosRepositoryImpl: PedidosRepository,
    @Named("ProductoRepositoryImpl") private val productoRepositoryImpl: ProductoRepository,
    @Named("TareasRepositoryImpl") private val tareasRepositoryImpl: TareasRepository,
    @Named("UsuarioRepositoryImpl") private val usuarioRepositoryImpl: UsuarioRepository,
    @Named("TurnoRepositoryImpl") private val turnoRepositoryImpl: TurnoRepository,
    @Named("UsuarioRepositoryKtorfit") private val ktorFitUsuario: UsuarioRepositoryKtorfit,
    @Named("RemoteCachedRepositoryUsuario") private val cacheRepositoryImpl: RemoteCachedRepositoryUsuario,
    @Named("UsuarioService") private val usuarioService: UsuariosService,
    var usuarioActual: Usuario? = null
) {

    //Maquina

    /**
     * Lista las maquinas disponibles en la base de datos siempre que el usuario no sea de tipo USUARIO.
     * Saltará un error si el usuario actual no tiene los permisos necesarios para buscar las maquinas.
     *
     * @return devuelve un flow de Maquina
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
     * Encuentra una maquina dado un id siempre que el usuario no sea de tipo USUARIO.
     *
     * @param id ID de la maquina a buscar
     * @return Una Maquina
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
     * Encuentra una maquinda dado su uuid que es su numero de serie y siempre que el usuario no sea de tipo USUARIO
     *
     * @param uuid UUID de la maquina a buscar
     * @return Una Maquina
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
     * Guarda una maquina en la base de datos siempre que el usuario no sea de tipo USUARIO
     *
     * @param maquina entidad maquina a guardar
     * @return La maquina si ha sido guardada correctamente
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
     * Borra una maquina de la base de datos siempre y cuando el usuario sea de tipo ADMIN
     * y la maquina no se esté utilizando actualmente
     *
     * @param maquina entidad maquina a borrar
     * @return devuelve true si borra la maquina y false si no ha podido guardarla
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
     * Lista todos los pedidos que existen en la base de datos siempre que el usuario no sea de tipo USUARIO
     *
     * @return una Flow de Pedidos
     */

    //Pedidos
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
     * Encuentra un pedido en base a su ID siempre que el usuario no sea de tipo USUARIO
     *
     * @param id ID del pedido a encontrar
     * @return Un Pedido
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
     * Encuuentra un pedido por su uuid siempre que el usuario no sea de tipo USUARIO
     *
     * @param uuid UUID del pedido a encontrar
     * @return Un Pedido
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
     * Guarda un pedido siempre que el usuario no sea de tipo ENCORDADOR.
     *
     * @param pedidos pedido a guardar
     * @return El pedido si ha sido guardado el producto
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
     * Borra un pedido siempre que el usuario sea de tipo ADMINISTRADOR.
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
     * Lista todos los productos
     *
     * @return Flow de productos
     */
    fun listarProductos(): Flow<Producto> {
        return productoRepositoryImpl.findAll()
    }

    /**
     * Encuentra un producto dado su id siempre que el usuario no sea de tipo USUARIO.
     *
     * @param id ID del producto a buscar
     * @return Un Producto
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
     * Encuentra un producto por su UUID siempre que el usuario no sea de tipo USUARIO
     *
     * @param uuid UUID del producto a buscar
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
     * Guarda un producto siempre que el usuario sea de tipo ADMINISTRADOR
     *
     * @param producto Entidad producto a guardar
     * @return El producto si ha sido guardado
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
     * Borra un producto siempre que el usuario sea de tipo ADMINISTRADOR
     *
     * @param producto Producto a guardar
     *  @return devuelve true si borra el producto y false si no ha podido guardarlo
     */
    suspend fun borrarProducto(producto: Producto): Boolean {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            productoRepositoryImpl.delete(producto)
        }else{
            logger.error { "Solo los administradores pueden eliminar productos" }
            false
        }

    }

    //Tareas

    /**
     * Lista las tareas existentes siempre que el usuario no sea de tipo USUARIO
     *
     * @return Flow de Tareas
     */
    fun listarTareas(): Flow<Tarea> {
        return if(usuarioActual!!.perfil != TipoPerfil.USUARIO) {
            tareasRepositoryImpl.findAll()
        }else{
            logger.error { "No tienes permiso para encontrar tareas" }
            emptyFlow()
        }
    }

    /**
     * Encuentra una tarea por su id siempre que el usuario no sea de tipo USUARIO
     *
     * @param id ID de la tarea a buscar
     * @return Una Tarea
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
     * Encuentra una tarea por UUID siempre que el usuario no sea de tipo USUARIO
     *
     * @param uuid UUID de la tarea a buscar
     * @return Una Tarea
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
     * Guarda una tarea siempre que el usuario no sea de tipo USUARIO.
     * En caso de que la tarea no este en un turno se podrá añadir ese turno, si no, no podrá añadirsea otro turno.
     *
     * @param tarea a guardar
     * @return La tarea si se ha guardado
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
     * Borra una tarea
     *
     * @param tarea Entidad tarea a guardar
     * @return devuelve un true si se borra y false si no lo ha borrado
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
     * Lista todos los usuarios existentes siempre que el usuario sea de tipo ADMINISTRADOR
     *
     * @return Un Flow de Usuarios
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
     * Encuentra un usuario dado su ID
     *
     * @param id ID del usuario a buscar
     * @return Un Usuario
     */
    suspend fun encontrarUsuarioID(id: Id<Usuario>): Usuario? {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            return usuarioRepositoryImpl.findById(id)
        } else{
            logger.error{"Solo un administrador puede encontrar usuarios"}
            null
        }
    }

    /**
     * Encuentra un usuario dado un uuid siempre que el usuario sea de tipo ADMINISTRADOR.
     *
     * @param uuid UUID del usuario a buscar
     * @return Un Usuario
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
     * Guarda un usuario siempre que el usuario no sea de tipo USUARIO
     *
     * @param usuario Entidad usuario a guardar
     * @return Un usuario si ha sido guardado
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
     * Borra el usuario siempre y cuando no tenga tareas pendientes y que el usuario sea de tipo ADMINISTRADOR
     *
     * @param usuario Entidad usuario a borrar
     * @return devuelve true si borra al usuario y false si no lo ha borrado
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

    //Turnos
    /**
     * Lista todos los turnos
     *
     * @return Un Flow de turnos
     */
    fun listarTurnos(): Flow<Turno> {
        return if(usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR){
            turnoRepositoryImpl.findAll()
        } else{
            logger.error{"Solo un administrador puede listar turnos"}
            emptyFlow()
        }
    }

    /**
     * Encuentra un turno dado su id siempre que el usuario sea de tipo ADMINISTRADOR.
     *
     * @param id ID de la tarea a buscar
     * @return  Un turno
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
     * Encuentra un turno por su UUID siempre que el usuario sea de tipo ADMINISTRADOR.
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
     * Guarda un turno siempre que el usuario sea de tipo ADMINISTRADOR.
     *
     * @param turno Entidad turno a guardar
     * @return El turno guardado
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
     * Borra un turno siempre que el usuario sea de tipo ADMINISTRADOR.
     *
     * @param turno Turno a borrar
     * @return true si borra el turno y false si no lo borra
     */
    suspend fun borrarTurno(turno: Turno): Boolean {
        return if (usuarioActual!!.perfil == TipoPerfil.ADMINISTRADOR) {
            turnoRepositoryImpl.delete(turno)
        } else {
            logger.debug("Solo un administrador puede eliminar el turno")
            false
        }
    }

    /**
     * Hace una peticion a la api con ktorfit
     *
     * @return Flow de usuarios
     */
    suspend fun encontrarUsuariosAPI(): Flow<Usuario> {
        return ktorFitUsuario.findAll()
    }

    /**
     * Guarda un usuario en la nube con ktorfit
     *
     * @param user Entidad usuario a guardar
     * @return el usuario
     */
    suspend fun guardarUsuariosAPI(user: Usuario): Usuario {
        return ktorFitUsuario.save(user)
    }

    /**
     * Añade un usuario a la caché
     *
     * @param user Usuario a guardar en la caché
     * @return Usuario
     */
    suspend fun añadirCacheUsuarios(user: Usuario): Usuario {
        return cacheRepositoryImpl.save(user)
    }

    /**
     * Un servicio para ver los cambios en tiempo real de usuario
     *
     * @return ChangeStreamPublisher de Usuarios
     */
    fun watchUsuarios(): ChangeStreamPublisher<Usuario> {
        logger.info("cambios en Tenistas")
        return UsuariosService().watch()
    }

    /**
     * Metodo para refrescar la cache
     *
     * @return Job
     */
    suspend fun refreshUsuarios(): Job {
        return cacheRepositoryImpl.refresh()
    }
}




