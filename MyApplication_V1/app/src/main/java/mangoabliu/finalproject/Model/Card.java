package mangoabliu.finalproject.Model;

/**
 * Created by Byanka on 28/11/2016.
 */

public class Card {

    private int CardID;
    private String CardName;
    private int CardHP;
    private int CardAttack;
    private int CardArmor;
    private int CardRarity;

    public Card(int _CardID,String _CardName,int _CardHP,int _CardAttack,int _CardArmor,int _CardRarity){
        CardID = _CardID;
        CardName = _CardName;
        CardHP = _CardHP;
        CardAttack = _CardAttack;
        CardArmor = _CardArmor;
        CardRarity = _CardRarity;
    }
    public int getCardID(){return CardID;}
    public String getCardName(){ return CardName;}
    public int getCardHP(){return CardHP;}
    public int getCardAttack(){return CardAttack;}
    public int getCardArmor(){return CardArmor;}
    public int getCardRarity(){return CardRarity;}
}
