package com.dummy.myerp.testbusiness.business;


import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bootstrapContext.xml")
@Sql({"classpath:sql/01_create_schema.sql", "classpath:sql/02_create_tables.sql", "classpath:sql/21_insert_data_demo.sql"})
@Transactional(propagation = Propagation.REQUIRED)
public class TestBusinessIntegration  extends BusinessTestCase {

    /**
     * Constructeur.
     */
    public TestBusinessIntegration() {
        super();
    }


    @Test
    public void testAddReference() throws NotFoundException, FunctionalException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));
       SpringRegistry.getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptable);
        Assert.assertEquals("AC-2018/00001", vEcritureComptable.getReference());

        SpringRegistry.getBusinessProxy().getComptabiliteManager().insertEcritureComptable(vEcritureComptable);

        EcritureComptable vEcritureComptableExisting = null;
        vEcritureComptableExisting = SpringRegistry.getBusinessProxy().getComptabiliteManager().getEcritureComptableById(-3);
        Assert.assertEquals("BQ-2016/00001", vEcritureComptableExisting.getReference());

        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptableExisting);

    }

    @Test
    public void testAddReferenceExistingReference() throws FunctionalException, NotFoundException{
        EcritureComptable vEcritureComptableExisting = null;
        vEcritureComptableExisting = SpringRegistry.getBusinessProxy().getComptabiliteManager().getEcritureComptableById(-5);
        Assert.assertEquals("BQ-2016/00002", vEcritureComptableExisting.getReference());
        SpringRegistry.getBusinessProxy().getComptabiliteManager().addReference(vEcritureComptableExisting);
        Assert.assertEquals("BQ-2016/00003", vEcritureComptableExisting.getReference());
        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptableExisting);
    }

    @Test(expected = FunctionalException.class)
    public void testCheckWithExistingReference() throws FunctionalException, NotFoundException{
        EcritureComptable vEcritureComptableExisting = null;
        vEcritureComptableExisting = SpringRegistry.getBusinessProxy().getComptabiliteManager().getEcritureComptableById(-3);
        Assert.assertNotEquals("BQ-2016/00005", vEcritureComptableExisting.getReference());
        vEcritureComptableExisting.setReference("BQ-2016/00002");
        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptableExisting);
    }


    @Test(expected = FunctionalException.class)
    public void testCheckEcritureComptable_RG_5_code() throws FunctionalException{
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        vEcritureComptable.setReference("BC-2018/00001");
        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void testCheckEcritureComptable_RG_5_date() throws FunctionalException{
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        vEcritureComptable.setReference("AC-2017/00001");
        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void testCheckEcritureComptable_RG_6() throws FunctionalException{
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, null,
                new BigDecimal(123)));

        vEcritureComptable.setReference("AC-2016/00001");
        SpringRegistry.getBusinessProxy().getComptabiliteManager().checkEcritureComptable(vEcritureComptable);
    }

    @Test
    public void testInsertEcritureComptable(){


    }


}
