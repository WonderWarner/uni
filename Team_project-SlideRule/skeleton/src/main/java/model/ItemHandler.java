package model;

/**
 * Az interfész megvalósítása lehetővé teszi, hogy egy tárgyakkal rendelkezni képes objektum új tárgyhoz férjen hozzá, vagy elhasznált tárgyat töröljön ki.
 */
public interface ItemHandler {

    /**
     * Tárgy hozzáadása az objektum tárgynyilvántartásába.
     * @param item a hozzáadni kívánt tárgy
     */
    public void addItem( Item item );

    /**
     * Tárgy törlése az objektum tárgynyilvántartásából.
     * @param item a törölni kívánt tárgy
     */
    public void removeItem( Item item );
}
