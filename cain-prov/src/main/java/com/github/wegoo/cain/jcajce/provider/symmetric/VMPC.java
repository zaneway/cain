package com.github.wegoo.cain.jcajce.provider.symmetric;

import com.github.wegoo.cain.crypto.CipherKeyGenerator;
import com.github.wegoo.cain.crypto.engines.VMPCEngine;
import com.github.wegoo.cain.crypto.macs.VMPCMac;
import com.github.wegoo.cain.jcajce.provider.config.ConfigurableProvider;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseKeyGenerator;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseMac;
import com.github.wegoo.cain.jcajce.provider.symmetric.util.BaseStreamCipher;
import com.github.wegoo.cain.jcajce.provider.util.AlgorithmProvider;

public final class VMPC
{
    private VMPC()
    {
    }
    
    public static class Base
        extends BaseStreamCipher
    {
        public Base()
        {
            super(new VMPCEngine(), 16);
        }
    }

    public static class KeyGen
        extends BaseKeyGenerator
    {
        public KeyGen()
        {
            super("VMPC", 128, new CipherKeyGenerator());
        }
    }

    public static class Mac
        extends BaseMac
    {
        public Mac()
        {
            super(new VMPCMac());
        }
    }

    public static class Mappings
        extends AlgorithmProvider
    {
        private static final String PREFIX = VMPC.class.getName();

        public Mappings()
        {
        }

        public void configure(ConfigurableProvider provider)
        {

            provider.addAlgorithm("Cipher.VMPC", PREFIX + "$Base");
            provider.addAlgorithm("KeyGenerator.VMPC", PREFIX + "$KeyGen");
            provider.addAlgorithm("Mac.VMPCMAC", PREFIX + "$Mac");
            provider.addAlgorithm("Alg.Alias.Mac.VMPC", "VMPCMAC");
            provider.addAlgorithm("Alg.Alias.Mac.VMPC-MAC", "VMPCMAC");

        }
    }
}
