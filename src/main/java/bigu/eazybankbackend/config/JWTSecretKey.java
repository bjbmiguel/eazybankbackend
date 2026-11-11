package bigu.eazybankbackend.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.util.Objects;

@UtilityClass
public class JWTSecretKey {

    public static SecretKey getSKey(String secretKey){
        Objects.requireNonNull(secretKey, "Secret Key cannot be null");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //Descodificamos a key, ou seja, para recuperaramos os bytes originais
        //String original = new String(keyBytes, StandardCharsets.UTF_8);
        //System.out.println("Original Key "+ original);
        return Keys.hmacShaKeyFor(keyBytes);//Validamos a key e criamos uma inst de SK com o Algoritimo a ser usado com base no size da SK
    }
}
