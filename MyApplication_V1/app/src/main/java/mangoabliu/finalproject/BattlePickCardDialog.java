package mangoabliu.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import mangoabliu.finalproject.Layout.CardLayout;
import mangoabliu.finalproject.Model.BattleModel;
import mangoabliu.finalproject.Model.Card;

/**
 * Created by Shi Zhongqi on 2016-11-29.
 */

public class BattlePickCardDialog extends Dialog {
    BattleModel battleModel;
    int index;
    ArrayList<Card> UserCards;
    private LinearLayout linearLayout_cardList;

    public BattlePickCardDialog(Context context, int style, int index) {
        super(context,style);
        battleModel=BattleModel.getInstance();
        this.index=index;
        Log.i("PickCardDialog","Object Create");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_battle_cardpick);
        Log.i("PickCardDialog","Create");
        linearLayout_cardList=(LinearLayout)findViewById(R.id.LinearLayout_battle_cardPickup);
        UserCards=battleModel.getUserCards();

        for (int i = 0; i < UserCards.size(); i++)
        {
            Log.i("PickCardDialog","Repeat,"+i);
            //1 就是可以选的，0是不可以选的
            if(battleModel.checkCardPick(UserCards.get(i).getCardID())==1) {
                CardLayout cardLayout = new CardLayout(this.getContext());
                Log.i("Card Dialog","HP:"+UserCards.get(i).getCardHP());
                cardLayout.setCardHP(UserCards.get(i).getCardHP());
                cardLayout.setCardAttack(UserCards.get(i).getCardAttack());
                cardLayout.setCardArmor(UserCards.get(i).getCardArmor());
                cardLayout.setCardBack(UserCards.get(i).getCardID());
                cardLayout.setOnClickListener(new clickListener_CardSelectCard(UserCards.get(i).getCardID()));
                linearLayout_cardList.addView(cardLayout);
            }
        }

    }

    private class clickListener_CardSelectCard implements View.OnClickListener {
        int thisCardID;

        public clickListener_CardSelectCard(int id){
            this.thisCardID=id;
        }

        public void onClick(View v) {
            Log.i("Card", "This Card ID: "+thisCardID);
            battleModel.pickCard(thisCardID,index);
            dismiss();
        }
    }



}
