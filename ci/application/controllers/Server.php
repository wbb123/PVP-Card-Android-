<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Server extends CI_Controller {

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

		$this->load->model('user_model','',true);
		$content_data['display_value']=$this->user_model->getall();

		$this->load->view('server',$content_data);

	}

	public function user(){
		$this->load->model('user_model','',true);
		$content_data['user']=$this->user_model->getall();

		$this->output
        ->set_content_type('application/json')
        ->set_output(json_encode($content_data));
	}

	public function register()
	{
		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句

		$json = $json_string;
		$paramemter['UserName'] = $json['UserName'];
        $paramemter['Password'] = md5($json['Password']);
        $paramemter['UserIconID'] = '1';
        $paramemter['Nickname']= $json['Nickname'];
        $paramemter['Alliance']=$json['Alliance'];
        $paramemter['PositionX']='20';
        $paramemter['PositionY']='20';
        $paramemter['WalkDistance']='0';
				$paramemter['CurrentLocationID']='1';
				$paramemter['TargetLocationID']='2';
				$paramemter['CurrentPositionX']='350';
				$paramemter['CurrentPositionY']='440';
        $paramemter['GpsLat']='0';
        $paramemter['GpsLon']='0';
        $data['code']="1";
        if($paramemter['UserName']!=""&&$paramemter['Password']!=""){
			$result = $this->user_model->register($paramemter);
			if($result['ret'] === 200)
			{
				$data['code'] = 0;
				$data['message'] = "Successful";
			}
			elseif($result['ret'] === 401)
			{
				$data['code'] = 2;
				$data['message'] = "Used UserName";
			}
			else{
				$data['code'] = 3;
				$data['message'] = "Fail";
			}
		}
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/registerM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);

	}

	public function registerM()
	{
		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserName'] = $json['UserName'];
        $paramemter['Password'] = md5($json['Password']);
        $paramemter['UserIconID'] = '1';
        $paramemter['Nickname']= $json['Nickname'];
        $paramemter['Alliance']=$json['Alliance'];
        $paramemter['PositionX']='20';
        $paramemter['PositionY']='20';
        $paramemter['WalkDistance']='0';
        $paramemter['GpsLat']='0';
        $paramemter['GpsLon']='0';
        $data['code']="1";
        if($paramemter['UserName']!=""&&$paramemter['Password']!=""){
			$result = $this->user_model->register($paramemter);
			if($result['ret'] === 200)
			{
				$data['code'] = 0;
				$data['message'] = "Successful";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));

			}
			elseif($result['ret'] === 401)
			{
				$data['code'] = 2;
				$data['message'] = "Used UserName";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
			else{
				$data['code'] = 3;
				$data['message'] = "Fail";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));

			}
		}
	}

	public function login()
	{
		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['UserName'] = $json['UserName'];
        $paramemter['Password'] = md5($json['Password']);
		$data['code']="1";
		if($paramemter['UserName']!=""&&$paramemter['Password']!=""){
			$result = $this->user_model->login($paramemter);
			if($result['ret']==200)
			{

				$data['code'] = 0;
				$data['UserInfo']=$result['UserInfo'];
				$data['LocationInfo']=$result['LocationInfo'];
				$data['LocationCardRelationInfo']=$result['LocationCardRelationInfo'];

			}else{
				$data['code']=2;
				$data['message'] = "UserName or Password not Match";

			}
		}
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/loginM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);

	}

	public function loginM()
	{
		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句

    	$json = json_decode($json_string, true);

    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserName'] = $json['UserName'];
        $paramemter['Password'] = md5($json['Password']);
        $data['code']="1";
        if($paramemter['UserName']!=""&&$paramemter['Password']!=""){

			$result = $this->user_model->login($paramemter);
			if($result['ret']==200)
			{

				$data['code'] = 0;
				$data['UserInfo']=$result['UserInfo'];
				$data['LocationInfo']=$result['LocationInfo'];
				$data['LocationCardRelationInfo']=$result['LocationCardRelationInfo'];

				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "UserName or Password not Match";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
		}
	}

	public function getUserCards(){

		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$result = $this->user_model->getUserCards($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['UserInfo']=$result['UserCards'];
			}else{
				$data['code']=2;
				$data['message'] = "No Such UserID";
			}

		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/getUserCardsM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);
	}

	public function getUserCardsM(){

		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$result = $this->user_model->getUserCards($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['UserInfo']=$result['UserCards'];
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "No Such UserID";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
	}

	public function updateUserStep(){

		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['WalkDistance']=$json['WalkDistance'];
		$result = $this->user_model->updateUserStep($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['WalkDistance']=$paramemter['WalkDistance'];
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
			}

		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateUserStepM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);
	}

	public function updateUserStepM(){

		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['WalkDistance']=$json['WalkDistance'];
		$result = $this->user_model->updateUserStep($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['Steps']=$paramemter['WalkDistance'];
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
	}
	public function updateTargetLocationID(){

		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['TargetLocationID']=$json['TargetLocationID'];
		$result = $this->user_model->updateTargetLocationID($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['TargetLocationID']=$paramemter['TargetLocationID'];
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
			}

		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateTargetLocationIDM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);
	}

	public function updateTargetLocationIDM(){

		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['TargetLocationID']=$json['TargetLocationID'];
		$result = $this->user_model->updateTargetLocationID($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['TargetLocationID']=$paramemter['TargetLocationID'];
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
	}

	public function updateCurrentLocationID(){

		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['CurrentLocationID']=$json['CurrentLocationID'];
		$result = $this->user_model->updateCurrentLocationID($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['CurrentLocationID']=$paramemter['CurrentLocationID'];
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
			}

		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateCurrentLocationIDM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);
	}

	public function updateCurrentLocationIDM(){

		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['CurrentLocationID']=$json['CurrentLocationID'];
		$result = $this->user_model->updateCurrentLocationID($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['CurrentLocationID']=$paramemter['CurrentLocationID'];
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
	}

	public function updateCurrentPosition(){

		$json_string = $this->input->post();

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['CurrentPositionX']=$json['CurrentPositionX'];
		$paramemter['CurrentPositionY']=$json['CurrentPositionY'];
		$result = $this->user_model->updateCurrentPosition($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['CurrentPositionX']=$paramemter['CurrentPositionX'];
				$data['CurrentPositionY']=$paramemter['CurrentPositionY'];
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
			}

		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateCurrentPositionM";

		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data, true);

		$this->load->view('result',$content_data);
	}

	public function updateCurrentPositionM(){

		$json_string = $this->input->raw_input_stream;

		$this->load->model('user_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		//$json = $json_string;
		$paramemter['UserID']=$json['UserID'];
		$paramemter['CurrentPositionX']=$json['CurrentPositionX'];
		$paramemter['CurrentPositionY']=$json['CurrentPositionY'];
		$result = $this->user_model->updateCurrentPosition($paramemter);
		if($result['ret']==200)
			{
				$data['code'] = 0;
				$data['CurrentPositionX']=$paramemter['CurrentPositionX'];
				$data['CurrentPositionY']=$paramemter['CurrentPositionY'];
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}else{
				$data['code']=2;
				$data['message'] = "Update Error";
				$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));
			}
	}


		public function updateUserCardRelation(){

			$json_string = $this->input->post();

			$this->load->model('user_model','',true);
			//传过来的是JSON String，用下面这句
	    	//$json = json_decode($json_string, true);
	    	//传过来的是JSON Object，用下面这句
			$json = $json_string;
			$paramemter['UserID']=$json['UserID'];
			$paramemter['CardID']=$json['CardID'];
			$result = $this->user_model->updateUserCardRelation($paramemter);
			if($result['ret']==200)
				{
					$data['code'] = 0;
					$data['UserID']=$paramemter['UserID'];
					$data['CardID']=$paramemter['CardID'];
				}else if($result['ret']==400){
					$data['code'] = 1;


				}	else{
					$data['code'] = 2;
					$data['message'] = "update error";
					}

			$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Server/updateUserCardRelationM";

			$content_data['display_value']['Input']=json_encode($json_string, true);

			$content_data['display_value']['Return']=json_encode($data, true);

			$this->load->view('result',$content_data);
		}

		public function updateUserCardRelationM(){

			$json_string = $this->input->raw_input_stream;

			$this->load->model('user_model','',true);
			//传过来的是JSON String，用下面这句
	    	$json = json_decode($json_string, true);
	    	//传过来的是JSON Object，用下面这句
			//$json = $json_string;
			$paramemter['UserID']=$json['UserID'];
			$paramemter['CardID']=$json['CardID'];
			$result = $this->user_model->updateUserCardRelation($paramemter);
			if($result['ret']==200)
				{
					$data['code'] = 0;
					$data['UserID']=$paramemter['UserID'];
					$data['CardID']=$paramemter['CardID'];
					$this->output
		        			->set_content_type('application/json')
		        			->set_output(json_encode($data));
				}else if($result['ret']==400)	{
					$data['code']=1;
					$this->output
									->set_content_type('application/json')
									->set_output(json_encode($data));


				}else{
				$data['code']=2;
				$data['message'] = "update error";
				$this->output
								->set_content_type('application/json')
								->set_output(json_encode($data));
						}
		}




}
