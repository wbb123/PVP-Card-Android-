package mangoabliu.finalproject.Layout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mangoabliu.finalproject.R;
/**
 * Created by SHI Zhongqi on 2016-11-29.
 */

public class CardLayout extends RelativeLayout {
    TextView cardHP;
    TextView cardAttack;
    TextView cardArmor;
    RelativeLayout cardBack;

    public CardLayout(Context context) {
        super(context);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_card, this);
        cardHP = (TextView) findViewById(R.id.card_hp);
        cardArmor=(TextView) findViewById(R.id.card_armor);
        cardAttack = (TextView) findViewById(R.id.card_attack);
        cardBack = (RelativeLayout)findViewById(R.id.card_layout);
    }

    public CardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_card, this);
        cardHP = (TextView) findViewById(R.id.card_hp);
        cardArmor=(TextView) findViewById(R.id.card_armor);
        cardAttack = (TextView) findViewById(R.id.card_attack);
        cardBack = (RelativeLayout)findViewById(R.id.card_layout);
    }

    public void setCardHP(int hp){
        cardHP.setText(Integer.toString(hp));
    }

    public void setCardAttack(int attack){
        cardAttack.setText(Integer.toString(attack));
    }

    public void setCardArmor(int armor){
        cardArmor.setText(Integer.toString(armor));
    }

    public void setCardBack(int CardID){
        Resources res = getResources();
        String[] CardsName = res.getStringArray(R.array.cards_name);
        int  DropCardId = cardBack.getContext().getResources().getIdentifier(CardsName[CardID-1], "drawable", cardBack.getContext().getPackageName());
        cardBack.setBackgroundResource(DropCardId);
    }

}
