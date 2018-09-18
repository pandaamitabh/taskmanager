Pre-requisite:
 
1.Make sure you’ve got Python & pip installed in your ubuntu 16.04
2.Task manager application is running fine and can be accessed using url- http://localhost:8080.
3.I have enabled the @RequestMapping annotation in TasksController.java to test the create task.

$sudo apt-get install libssl-dev openssl
$wget https://www.python.org/ftp/python/3.5.0/Python-3.5.0.tgz
$tar xzvf Python-3.5.0.tgz
$cd Python-3.5.0
$./configure
$make
$sudo make install

Steps:

Follow below instructions/commands for setting virtual environment.

1.Install virtual environment

$ sudo pip install virtualenv
 
2. Create virtual environment

$virtualenv -p python3.5 venv

3. Activate virtual environment

$source venv/bin/activate

4. Install requests package

$pip install requests

5. Install psycopg2 package

$pip install psycopg2

6. Install dependent psycopg2-binary package

$pip install psycopg2-binary

7. Check python 3.5.0 is properly set in virtual environment

8. Check the python path and set the shebang properly in the script (IntegrationTest.py).

9. Place the IntegrationTest.py in the same directory.Give execution right(chmod +x) to the script.

10. Execute the script by using below command,

$python IntegrationTest.py 
