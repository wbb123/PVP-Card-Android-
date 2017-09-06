<?php
    
class Game_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }


    function myTurn($parameter){
        $this->db->trans_start();
        $this->db->select('*');
        $this->db->from('Room'.$parameter['RoomID']);       
        $this->db->order_by('PlayID','desc');
        $this->db->limit(1);
        $result['LastPlay']=$this->db->get()->row_array();

        $this->db->trans_complete(); 
       	if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
            }
        $result['ret'] = 200;
        if($result['LastPlay']['UserID']==$parameter['UserID'])
        	$result['ret'] = 201; 

        return $result;
    }


    function playCard($parameter){
		$this->db->trans_start();
		$this->db->select('*');
        $this->db->from('Cards');
        $this->db->where('CardID',$parameter['Player1CardID']);
        $Card1=$this->db->get()->row_array();
        $this->db->select('*');
        $this->db->from('Cards');
        $this->db->where('CardID',$parameter['Player2CardID']);
        $Card2=$this->db->get()->row_array();

		$this->db->select('*');

        $this->db->from('Room'.$parameter['RoomID']);
           
        $this->db->order_by('PlayID','desc');
        $this->db->limit(1);

        $LastData=$this->db->get()->row_array();
 		if($LastData['UserID']==$parameter['UserID']){
        	$result['ret'] = 201;
        	return $result; 
        }
        $this->db->trans_complete(); 
       	if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
        } 
        $result['ret'] = 200;
		if($parameter['Player']==1){
			//攻击力x(1-(防御力/(400+防御力)))
			$result['Hurt']=$Card1['CardAttack']*(1-($Card2['CardArmor']/($Card2['CardArmor']+50)));
			$LastData['UserID']=$parameter['UserID'];
			$LastData['FromNum']=$parameter['Player1CardNum'];
			$LastData['ToNum']=$parameter['Player2CardNum'];
			$LastData['Player2Card'.$parameter['Player2CardNum']."HP"]=$LastData['Player2Card'.$parameter['Player2CardNum']."HP"]-$result['Hurt'];
			$result['Win']='0';
			if($LastData['Player2Card1HP']<=0){
				if($LastData['Player2Card2HP']<=0){
					if($LastData['Player2Card3HP']<=0){
						$result['Win']='1';
					}
				}
			}
		}
		else{
			//攻击力x(1-(防御力/(400+防御力)))
			$result['Hurt']=$Card2['CardAttack']*(1-($Card1['CardArmor']/($Card1['CardArmor']+50)));
			$LastData['UserID']=$parameter['UserID'];
			$LastData['FromNum']=$parameter['Player2CardNum'];
			$LastData['ToNum']=$parameter['Player1CardNum'];
			$LastData['Player1Card'.$parameter['Player1CardNum']."HP"]=$LastData['Player1Card'.$parameter['Player1CardNum']."HP"]-$result['Hurt'];
			$result['Win']='0';
			if($LastData['Player1Card1HP']<=0){
				if($LastData['Player1Card2HP']<=0){
					if($LastData['Player1Card3HP']<=0){
						$result['Win']='2';
					}
				}
			}
		}
		$LastData['PlayID']=$LastData['PlayID']+1;
		$this->db->insert('Room'.$parameter['RoomID'], $LastData);
		return $result;
    }

    function setCards($parameter){
    	$data['CardID1'] = $parameter['CardID1'];
        $data['CardID2'] = $parameter['CardID2'];
        $data['CardID3'] = $parameter['CardID3'];
        $data['Ready'] = 1;
        $where = "RoomID=".$parameter['RoomID']." AND UserID=".$parameter['UserID'];
        $this->db->where($where);

        $this->db->trans_start();
        $this->db->update('Rooms', $data);
        $this->db->trans_complete(); 
       	if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
            } 
        $result['ret'] = 200;
        $result['RoomID']=$parameter['RoomID'];
        return $result;
    }

    function isRoomReady($parameter){
    	$this->db->trans_start();
    	$this->db->select('*');
        $this->db->from('Rooms');
        $this->db->where('RoomID',$parameter['RoomID']);
        $query=$this->db->get();
        $NumberOfPlayer = $query->num_rows();
        $query_result=$query->result_array();
  		$this->db->trans_complete(); 
        if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
        } 
     

        if($NumberOfPlayer==2){
        	$result['ret'] = 200;
        	$this->db->select('User.UserID, User.UserName');
	        $this->db->from('User');
	        $where = "UserID=".$query_result[0]['UserID']." Or UserID='".$query_result[1]['UserID']."'";
	        $this->db->where($where);
	        $query=$this->db->get();
	        $PlayerInfo = $query->result_array();
        	$result['RoomID']=$parameter['RoomID'];
        	$result['UserName']=$PlayerInfo;
        	return $result;
        }
        else{
        	$result['ret']=400;
        	return $result;
        }
    }

    function isFightReady($parameter){
		$this->db->trans_start();
    	$this->db->select('*');
        $this->db->from('Rooms');
        $where = "RoomID=".$parameter['RoomID']." AND Ready='1'";
        $this->db->where($where);
        $query=$this->db->get();
        $query_result=$query->result_array();
        $NumberOfPlayer = $query->num_rows();
        //$this->db->where('RoomID', $parameter['RoomID']);
 		//$this->db->delete('Rooms'); 
 		$tableFound=1;
        if($NumberOfPlayer==2){

        	$iftableexist = $this->db->table_exists('Room'.$parameter['RoomID']);
			
			if (!$iftableexist) {
			    $tableFound=0;// echo "No Table Found";
			}
			if($tableFound==0){
	        	$this->load->dbforge();
	        	$fields = array(
						        'PlayID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						                'auto_increment' => TRUE,
						        ),
						        'Player1ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'UserID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card1ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card1HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card2ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card2HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card3ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player1Card3HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card1ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card1HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card2ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card2HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card3ID' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'Player2Card3HP' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'FromNum' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),
						        'ToNum' => array(
						                'type' => 'INT',
						                'constraint' => 11,
						        ),     
						);
				$this->dbforge->add_field($fields);
				$this->dbforge->add_key('PlayID', TRUE);
				$this->dbforge->create_table('Room'.$parameter['RoomID'], TRUE);
			}
		

     		$this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[0]['CardID1']);
	        $Card1=$this->db->get()->row_array();
	        $this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[0]['CardID2']);
	        $Card2=$this->db->get()->row_array();
	        $this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[0]['CardID3']);
	        $Card3=$this->db->get()->row_array();
	        $this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[1]['CardID1']);
	        $Card4=$this->db->get()->row_array();
	        $this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[1]['CardID2']);
	        $Card5=$this->db->get()->row_array();
	        $this->db->select('*');
	        $this->db->from('Cards');
	        $this->db->where('CardID',$query_result[1]['CardID3']);
	        $Card6=$this->db->get()->row_array();


     		$data = array(
					        'Player1ID' => $query_result[0]['UserID'],
					        'Player2ID' => $query_result[1]['UserID'],
					        'UserID' => $query_result[1]['UserID'],
					        'Player1Card1ID' => $query_result[0]['CardID1'],
					        'Player1Card1HP' => $Card1['CardHP'],
					        'Player1Card2ID' => $query_result[0]['CardID2'],
					        'Player1Card2HP' => $Card2['CardHP'],
					        'Player1Card3ID' => $query_result[0]['CardID3'],
					        'Player1Card3HP' => $Card3['CardHP'],
					        'Player2Card1ID' => $query_result[1]['CardID1'],
					        'Player2Card1HP' => $Card4['CardHP'],
					        'Player2Card2ID' => $query_result[1]['CardID2'],
					        'Player2Card2HP' => $Card5['CardHP'],
					        'Player2Card3ID' => $query_result[1]['CardID3'],
					        'Player2Card3HP' => $Card6['CardHP'],
					        'FromNum' => 0,
					        'ToNum' => 0,     
					);
     		if($tableFound==0)
     			$this->db->insert('Room'.$parameter['RoomID'], $data);

     	
			$result['ret'] = 200;
        	$result['RoomInfo']=$query_result;
        	$result['CardInfo'][0]=$Card1;
        	$result['CardInfo'][1]=$Card2;
        	$result['CardInfo'][2]=$Card3;
        	$result['CardInfo'][3]=$Card4;
        	$result['CardInfo'][4]=$Card5;
        	$result['CardInfo'][5]=$Card6;


        	return $result;
        }
        else{
        	$result['ret'] =400;
        	return $result;
        }

    }


    function applyForFight($paramemter)
    {
        $this->db->trans_start();

        $this->db->select('*');
        $this->db->from('Rooms');
        $query=$this->db->get();
        $NumberOfRooms = $query->num_rows();
        if($NumberOfRooms%2==0){
        	$data=array('Remark'=>1);
       		$this->db->insert('RoomIDGenerator',$data);
        
	        $this->db->select('RoomID');
	        $this->db->from('RoomIDGenerator');
	        $this->db->order_by('RoomID','desc');
	        $this->db->limit(1);

	        $query = $this->db->get();
	        $from_query = $query->row_array();

	        $data = array(
	                'UserID'  => $paramemter['UserID'],  
	                'RoomID'  => $from_query['RoomID'],
	                'Ready' => 0,
	            ); 

		    $this->db->insert('Rooms', $data);
	    }
	    else{
	    	$this->db->select('*');
	    	$this->db->from('(SELECT  `RoomID` , COUNT(  `RoomID` ) AS  `Total` FROM  `Rooms` GROUP BY  `RoomID`) As A');
       		$this->db->where('A.Total',1);
        	$this->db->order_by('RoomID','desc');
        	$this->db->limit(1);
        	$query = $this->db->get();
	        $from_query = $query->row_array();
	        $data = array(
	                'UserID'  => $paramemter['UserID'] ,  
	                'RoomID'  => $from_query['RoomID']
	            ); 

		    $this->db->insert('Rooms', $data);

	    }

       	$this->db->trans_complete(); 
       	if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
            } 
        $result['ret'] = 200;
        $result['RoomID']=$from_query['RoomID'];
        return $result;
    }


}