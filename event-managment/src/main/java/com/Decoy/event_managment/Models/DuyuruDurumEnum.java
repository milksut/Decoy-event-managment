package com.Decoy.event_managment.Models;

public enum DuyuruDurumEnum
{

    Bugun(0),
    Yarin(1),
    BuHafta(2),
    BuAy(3),
    UcAy(4),
    BuYil(5),
    BirYildanFazla(6),
    TarihiGecmis(7),
    TarihiBelirsiz(8);

    public final int value;

    DuyuruDurumEnum(int i)
    {
        this.value = i;
    }

    // int → Durum
    public static DuyuruDurumEnum fromValue(int value) {
        for (DuyuruDurumEnum d : values())
        {
            if (d.value == value) return d;
        }
        throw new IllegalArgumentException("Geçersiz durum: " + value);
    }
}
