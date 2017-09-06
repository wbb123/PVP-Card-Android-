<?php

class User_model extends CI_Model {

    function __construct()
    {
        parent::__construct();
    }

    function getall(){
        $this->db->select('*');
        $this->db->from('User');
        $query = $this->db->get();
        return  $query->result();
    }

    function register($paramemter){
        $query = $this->db->get_where('User', array('UserName' => $paramemter['UserName']),   1);
        if ($query->num_rows() > 0)
        {
            $result['ret'] = 401;
            return $result;
        }
        else
        {

            $data = array(
                'UserName'    => $paramemter['UserName'] ,
                'UserIconID'  => $paramemter['UserIconID'],
                'Password'    => $paramemter['Password'],
                'Nickname'    => $paramemter['Nickname'],
                'Alliance'    => $paramemter['Alliance'],
                'WalkDistance'=> $paramemter['WalkDistance'],
            );
            $this->db->trans_start();
            $this->db->insert('User', $data);
            $query = $this->db->get_where('User', array('UserName' => $paramemter['UserName']),   1);
            $result['UserInfo'] = $query->row_array();

            $cardData[0] = array(
                'UserID'    => $result['UserInfo']['UserID'] ,
                'CardID'  => '16'

            );
            $cardData[1] = array(
                'UserID'    => $result['UserInfo']['UserID'] ,
                'CardID'  => '17'

            );
            $cardData[2]= array(
                'UserID' => $result['UserInfo']['UserID'] ,
                'CardID'  => '18'
            );
            $this->db->insert_batch('UserCardRelation', $cardData);

            $this->db->trans_complete();
            if ($this->db->trans_status() === FALSE) {
               //检测Insert是否Fail
               $result['ret'] = 400;
               return $result;
            } else {

            }
            $result['ret'] = 200;
            return $result;

        }

    }

    function login($paramemter)
    {
        $query = $this->db->get_where('User', array('UserName' => $paramemter['UserName']),   1);
        if ($query->num_rows() > 0)
        {
            $result['UserInfo'] = $query->row_array();
            $result['ret']=200;
            $this->db->select('*');
            $this->db->from('Location');
            $query = $this->db->get();
            $result['LocationInfo'] = $query->result_array();

            $this->db->select('*');
            $this->db->from('LocationCardRelation as a');
            $this->db->join('Location as b','a.LocationID = b.LocationID');
            $query = $this->db->get();
            $result['LocationCardRelationInfo'] = $query->result_array();
            
            $password = $paramemter['Password'];
            if($result['UserInfo']['Password'] != $password){
                $result['ret'] = 401;
            }

            return $result;
        }else{

            $result['ret']=404;
            return $result;
        }
    }

    function getUserCards($paramemter)
    {

        $this->db->select('*');
        $this->db->from('UserCardRelation as a');
        $this->db->join('Cards as b', 'a.CardID = b.CardID');
        $this->db->where('a.UserID', $paramemter['UserID']);
        $query = $this->db->get();
        if ($query->num_rows() > 0)
        {
            $result['UserCards'] = $query->result_array();
            $result['ret']=200;
            return $result;
        }else{

            $result['ret']=404;
            return $result;
        }

    }

    function updateUserStep($paramemter)
    {

        $this->db->where('UserID', $paramemter['UserID']);
        $this->db->update('User', $paramemter);

        $this->db->trans_complete();

        if ($this->db->trans_status() === FALSE)
        {
            $result['ret']=200;
            return $result;
        }
        $result['ret']=200;
        return $result;
    }

    function updateTargetLocationID($paramemter)
    {

        $this->db->where('UserID', $paramemter['UserID']);
        $this->db->update('User', $paramemter);

        $this->db->trans_complete();

        if ($this->db->trans_status() === FALSE)
        {
            $result['ret']=200;
            return $result;
        }
        $result['ret']=200;
        return $result;
    }
    function updateCurrentLocationID($paramemter)
    {

        $this->db->where('UserID', $paramemter['UserID']);
        $this->db->update('User', $paramemter);

        $this->db->trans_complete();

        if ($this->db->trans_status() === FALSE)
        {
            $result['ret']=200;
            return $result;
        }
        $result['ret']=200;
        return $result;
    }
    function updateCurrentPosition($paramemter)
    {

        $this->db->where('UserID', $paramemter['UserID']);
        $this->db->update('User', $paramemter);

        $this->db->trans_complete();

        if ($this->db->trans_status() === FALSE)
        {
            $result['ret']=200;
            return $result;
        }
        $result['ret']=200;
        return $result;
    }

    function updateUserCardRelation($paramemter)
    {

      $data = array(
          'UserID'  => $paramemter['UserID'],
          'CardID'  => $paramemter['CardID']);

      $this->db->select('*');
      $this->db->from('UserCardRelation');
      $this->db->where('UserID', $paramemter['UserID']);
      $this->db->where('CardID', $paramemter['CardID']);
      $query = $this->db->get();
      if ($query->num_rows() > 0)
      {
        $result['ret'] = 400;
        return $result;

      }else{
        $this->db->trans_start();
        $this->db->insert('UserCardRelation', $data);
        $this->db->trans_complete();
        if ($this->db->trans_status() === FALSE)
        {
            $result['ret'] = 401;
            return $result;
        }
        $result['ret']=200;
        return $result;
      }


    }




}
