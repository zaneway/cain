package com.github.wegoo.cain.oer.its.etsi102941;

import com.github.wegoo.cain.asn1.ASN1Sequence;
import com.github.wegoo.cain.oer.its.etsi103097.EtsiTs103097DataSigned;
import com.github.wegoo.cain.oer.its.ieee1609dot2.Ieee1609Dot2Content;

public class CaCertificateRequestMessage
    extends EtsiTs103097DataSigned
{

    public CaCertificateRequestMessage(Ieee1609Dot2Content content)
    {
        super(content);
    }

    protected CaCertificateRequestMessage(ASN1Sequence src)
    {
        super(src);
    }

    public static CaCertificateRequestMessage getInstance(Object o)
    {
        if (o instanceof CaCertificateRequestMessage)
        {
            return (CaCertificateRequestMessage)o;
        }
        if (o != null)
        {
            return new CaCertificateRequestMessage(ASN1Sequence.getInstance(o));
        }
        return null;
    }


}
