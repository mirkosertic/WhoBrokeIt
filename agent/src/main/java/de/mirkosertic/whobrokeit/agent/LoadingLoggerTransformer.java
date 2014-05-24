package de.mirkosertic.whobrokeit.agent;

import org.aspectj.weaver.loadtime.Aj;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadingLoggerTransformer implements ClassFileTransformer {

    private final static Logger LOGGER = Logger.getLogger(LoadingLoggerTransformer.class.getName());

    private final Aj aj;

    LoadingLoggerTransformer() {
        aj = new Aj();
        aj.initialize();
    }

    @Override
    public byte[] transform(ClassLoader aLoader, String aClassName, Class<?> aClassBeingRedifined, ProtectionDomain aProtectionDomain, byte[] aClassData) throws IllegalClassFormatException {
        LOGGER.info("Retransforming class with AspectJ : " + aClassName.replace('/', '.'));
        try {
            return aj.preProcess(aClassName, aClassData, aLoader, aProtectionDomain);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot transform class data for " + aClassName, e);
        }
        return aClassData;
    }
}
