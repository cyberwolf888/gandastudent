package com.gandaedukasi.gandaedukasi.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cadis-Etrama on 7/13/2016.
 */
public class ProgramEdukasi {

    public int id;
    public String name, desc;

    public static List<ProgramEdukasi> getPE(){
        List<ProgramEdukasi> PEList = new ArrayList<>();

        ProgramEdukasi pe = new ProgramEdukasi();
        pe.id = 1;
        pe.name = "Ganda Kids";
        pe.desc = "Ganda Kids Desc";
        PEList.add(pe);

        pe = new ProgramEdukasi();
        pe.id = 2;
        pe.name = "Ganda Privat";
        pe.desc = "Ganda Privat Desc";
        PEList.add(pe);

        pe = new ProgramEdukasi();
        pe.id = 3;
        pe.name = "Ganda Talent";
        pe.desc = "Ganda Talent Desc";
        PEList.add(pe);

        pe = new ProgramEdukasi();
        pe.id = 4;
        pe.name = "Ganda English";
        pe.desc = "Ganda English Desc";
        PEList.add(pe);

        return PEList;
    }
}
