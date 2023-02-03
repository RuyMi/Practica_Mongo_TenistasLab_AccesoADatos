package es.ar.practica_spring_tenistaslab.services.password

import com.toxicbakery.bcrypt.Bcrypt


/**
 * Password
 *
 */
class Password {
    /**
     * Encripta la cadena con hash 12 rondas
     *
     * @param originalString Cadena a encriptar
     * @return la cadena encriptada
     */
    fun encriptar(originalString: String): ByteArray {
        return Bcrypt.hash(originalString, 12)
    }

    /**
     * Verifica que la cadena coincide con su hash
     *
     * @param originalString Cadena original
     * @param byteHash hash que se esperaba
     * @return true si la cadena coincide
     */
    fun verificar(originalString: String, byteHash: ByteArray): Boolean{
        return Bcrypt.verify(originalString, byteHash)
    }
}