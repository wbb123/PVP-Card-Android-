<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Game extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see https://codeigniter.com/user_guide/general/urls.html
	 */


	public function index()
	{

		$this->load->view('game');

	}

	public function getTime(){
		$data['code'] = 1;
		$data['time'] = time();	
		$this->output
        			->set_content_type('application/json')
        			->set_output(json_encode($data));	
	}

	public function myTurn(){
    	$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$parameter['RoomID'] = $json['RoomID'];
		$parameter['UserID'] = $json['UserID'];
		$result = $this->game_model->myTurn($parameter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['LastPlay'] = $result['LastPlay'];
		}
		else{
			$data['code'] = 1;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/myTurnM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
    }

    public function myTurnM(){
    	$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;

		$parameter['RoomID'] = $json['RoomID'];
		$parameter['UserID'] = $json['UserID'];
		$result = $this->game_model->myTurn($parameter);
		if($result['ret'] === 200){
			$data['code'] = 0;	
			$data['LastPlay'] = $result['LastPlay'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
		else{
			$data['code'] = 1;
			$data['message'] = "Waiting";	
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));	
		}
	
    }

	public function playCard(){
    	$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$parameter['RoomID'] = $json['RoomID'];
		$parameter['UserID'] = $json['UserID'];
		$parameter['Player1CardID']= $json['Player1CardID'];
		$parameter['Player2CardID']= $json['Player2CardID'];
		$parameter['Player1CardNum']= $json['Player1CardNum'];
		$parameter['Player2CardNum']= $json['Player2CardNum'];
		$parameter['Player']= $json['Player'];
       
		$result = $this->game_model->playCard($parameter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['Hurt'] = $result['Hurt'];
			$data['Win']=$result['Win'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/playCardM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
    }

    public function playCardM(){
    	$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;

		$parameter['RoomID'] = $json['RoomID'];
		$parameter['UserID'] = $json['UserID'];
		$parameter['Player1CardID']= $json['Player1CardID'];
		$parameter['Player2CardID']= $json['Player2CardID'];
		$parameter['Player1CardNum']= $json['Player1CardNum'];
		$parameter['Player2CardNum']= $json['Player2CardNum'];
		$parameter['Player']= $json['Player'];
       
		$result = $this->game_model->playCard($parameter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['Hurt'] = $result['Hurt'];
			$data['Win']=$result['Win'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Not Your Turn";		
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
    }


	public function isFightReady(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
       
		$result = $this->game_model->isFightReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomInfo'] = $result['RoomInfo'];
			$data['CardInfo'] = $result['CardInfo'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isFightReadyM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
	}

	public function isFightReadyM(){
		$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
       
		$result = $this->game_model->isFightReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomInfo'] = $result['RoomInfo'];
			$data['CardInfo'] = $result['CardInfo'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));		
		}
	}

	public function setCards(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
		$paramemter['UserID'] = $json['UserID'];
		$paramemter['CardID1'] = $json['CardID1'];
		$paramemter['CardID2'] = $json['CardID2'];
		$paramemter['CardID3'] = $json['CardID3'];

       
		$result = $this->game_model->setCards($paramemter);

		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/setCardsM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);


	}

	public function setCardsM(){
		$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
		$paramemter['UserID'] = $json['UserID'];
		$paramemter['CardID1'] = $json['CardID1'];
		$paramemter['CardID2'] = $json['CardID2'];
		$paramemter['CardID3'] = $json['CardID3'];
       
		$result = $this->game_model->setCards($paramemter);

		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Set Card Fail, Please Try Again";
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));		
		}
		
	}

	public function isRoomReady(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
       
		$result = $this->game_model->isRoomReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
			$data['UserName'] = $result['UserName'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isRoomReadyM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
	}

	public function isRoomReadyM(){
		$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;

		$paramemter['RoomID'] = $json["RoomID"];
       
		$result = $this->game_model->isRoomReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
			$data['UserName'] = $result['UserName'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));		
		}
	}

	public function applyForFight()
	{
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
    
		$json = $json_string;
		$paramemter['UserID'] = $json['UserID'];
       
		$result = $this->game_model->applyForFight($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;
			$data['RoomID'] = $result['RoomID'];	
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Fail";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/applyForFightM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
		
	}

	public function applyForFightM()
	{
		$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句 
		//$json = $json_string;
		$paramemter['UserID'] = $json['UserID'];
       
		$result = $this->game_model->applyForFight($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;
			$data['RoomID'] = $result['RoomID'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));	
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Fail";	
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));	
		}
		
	}

}
