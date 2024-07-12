package com.github.wegoo.cain.jcajce.provider.symmetric;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.spec.IvParameterSpec;

import com.github.wegoo.cain.crypto.BlockCipher;
import com.github.wegoo.cain.crypto.CipherKeyGenerator;
import com.github.wegoo.cain.crypto.CryptoServicesRegistrar;
import com.github.wegoo.cain.crypto.engines.NoekeonEngine;
import com.github.wegoo.cain.crypto.generators.Poly1305KeyGenerator;
import com.github.wegoo.cain.crypto.macs.GMac;
import com.github.wegoo.cain.crypto.modes.GCMBlockCipher;
import com.github.wegoo.cain.jcajce.provider.config.ConfigurableProvider;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseBlockCipher;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseKeyGenerator;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseMac;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BlockCipherProvider;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.IvAlgorithmParameters;

public final class Noekeon
{
    private Noekeon()
    {
    }

    public static class ECB
        extends BaseBlockCipher
    {
        public ECB()
        {
            super(new BlockCipherProvider()
            {
                public BlockCipher get()
                {
                    return new NoekeonEngine();
                }
            });
        }
    }

    public static class KeyGen
        extends BaseKeyGenerator
    {
        public KeyGen()
        {
            super("Noekeon", 128, new CipherKeyGenerator());
        }
    }

    public static class GMAC
        extends BaseMac
    {
        public GMAC()
        {
            super(new GMac(new GCMBlockCipher(new NoekeonEngine())));
        }
    }

    public static class Poly1305
        extends BaseMac
    {
        public Poly1305()
        {
            super(new com.github.wegoo.cain.crypto.macs.Poly1305(new NoekeonEngine()));
        }
    }

    public static class Poly1305KeyGen
        extends BaseKeyGenerator
    {
        public Poly1305KeyGen()
        {
            super("Poly1305-Noekeon", 256, new Poly1305KeyGenerator());
        }
    }

    public static class AlgParamGen
        extends BaseAlgorithmParameterGenerator
    {
        protected void engineInit(
            AlgorithmParameterSpec genParamSpec,
            SecureRandom random)
            throws InvalidAlgorithmParameterException
        {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Noekeon parameter generation.");
        }

        protected AlgorithmParameters engineGenerateParameters()
        {
            byte[] iv = new byte[16];

            if (random == null)
            {
                random = CryptoServicesRegistrar.getSecureRandom();
            }

            random.nextBytes(iv);

            AlgorithmParameters params;

            try
            {
                params = createParametersInstance("Noekeon");
                params.init(new IvParameterSpec(iv));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e.getMessage());
            }

            return params;
        }
    }

    public static class AlgParams
        extends IvAlgorithmParameters
    {
        protected String engineToString()
        {
            return "Noekeon IV";
        }
    }

    public static class Mappings
        extends SymmetricAlgorithmProvider
    {
        private static final String PREFIX = Noekeon.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {

            provider.addAlgorithm("AlgorithmParameters.NOEKEON", PREFIX + "$AlgParams");

            provider.addAlgorithm("AlgorithmParameterGenerator.NOEKEON", PREFIX + "$AlgParamGen");

            provider.addAlgorithm("Cipher.NOEKEON", PREFIX + "$ECB");

            provider.addAlgorithm("KeyGenerator.NOEKEON", PREFIX + "$KeyGen");

            addGMacAlgorithm(provider, "NOEKEON", PREFIX + "$GMAC", PREFIX + "$KeyGen");
            addPoly1305Algorithm(provider, "NOEKEON", PREFIX + "$Poly1305", PREFIX + "$Poly1305KeyGen");
        }
    }
}
