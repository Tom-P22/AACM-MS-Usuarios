package cl.municipalidad.usuarios.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutValidator implements jakarta.validation.ConstraintValidator<RutValido, String> {

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        if (rut == null ||rut.trim().isEmpty()) {
            return true;
        }

        try {

        rut = rut.toUpperCase().replaceAll("[^0-9K]", "");


        if (rut.length() < 8) {
            return false;
        }

        String numero = rut.substring(0, rut.length() - 1);
        char digitoVerificador = rut.charAt(rut.length() - 1);

        int rutNumerico = Integer.parseInt(numero);

        int m = 0, s = 1;
        for (; rutNumerico != 0; rutNumerico /= 10) {
            s = (s + rutNumerico % 10 * (9 - m++ % 6)) % 11;
        }

        char dvEsperado = (char) (s != 0 ? s + 47 : 75);

        return dvEsperado == digitoVerificador;
        } 
        catch (NumberFormatException e){
        return false;
        }
    }
}

