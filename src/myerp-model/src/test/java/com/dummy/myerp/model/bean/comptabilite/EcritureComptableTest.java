package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        BigDecimal bd1 = new BigDecimal (34100).movePointLeft(2);
        BigDecimal bd2 = new BigDecimal (341);
        Assert.assertEquals(bd1, vEcriture.getTotalDebit());
        Assert.assertEquals(bd2, vEcriture.getTotalCredit());
        Assert.assertEquals(0, vEcriture.isEquilibree()); //a.compareTo(b);  // returns (-1 if a < b), (0 if a == b), (1 if a > b)

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        bd1 = new BigDecimal (31);
        bd2 = new BigDecimal (33);
        Assert.assertEquals(bd1, vEcriture.getTotalDebit());
        Assert.assertEquals(bd2, vEcriture.getTotalCredit());
        Assert.assertEquals(-1, vEcriture.isEquilibree()); //a.compareTo(b);  // returns (-1 if a < b), (0 if a == b), (1 if a > b)
        System.out.println(vEcriture.toString());

        LigneEcritureComptable ligneEcritureComptabletest = this.createLigne(1, "200.50", null);
        System.out.println(ligneEcritureComptabletest.toString());

        JournalComptable journalComptable = new JournalComptable("AC", "Achat");
        System.out.println(journalComptable.toString());

        CompteComptable compteComptable = new CompteComptable(401);
        System.out.println(compteComptable.toString());
        compteComptable.setLibelle("Libelle");
        Assert.assertEquals("Libelle", compteComptable.getLibelle());

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2018, 2);
        System.out.println(sequenceEcritureComptable.toString());
    }

}
