#!/usr/local/bin/python3.5

'''
Integration test for REST API GET,POST
Author: Amitabh Panda

'''

import requests
import psycopg2


def test_get_task(url):
    r = requests.get(url)
    assert r.status_code == 200, "Error in GET request"
    assert r.headers['Content-Type'] == 'application/json;charset=UTF-8', "Error in headers"
    return r.json()


def test_create_task(url, task, headers):
    resp = requests.post(url, json=task, headers=headers)
    assert resp.status_code == 200, "Error in POST request"


def test_validate_json_data(task):
    json_get = test_get_task(url + "/list")[-1]
    print("RESTFUL GET Data>>>>")
    print(json_get)
    if json_get['title'] == task['title'] and json_get['description'] == task['description'] and \
            json_get['priority'] == task['priority'] and json_get['status'] == task['status']:
        print("POST was successful")
    else:
        print("POST was not successful")


def test_database_fetch(param):
    conn = psycopg2.connect(param[0])
    cur = conn.cursor()
    cur.execute(param[1])
    return cur.fetchall()[-1]


def test_validate_database_entries(database_values, task):
    print("PostgreSQL data>>>>")
    print(database_values)
    print("RESTFUL POST data>>>>")
    print(task)
    if database_values[0] == task['title'] and database_values[1] == task['description'] and \
            database_values[2] == task['priority'] and database_values[3] == task['status']:
        print("Database entries were validated successfully")
    else:
        print("Database entries were not matching")


if __name__ == '__main__':
    url = "http://localhost:8080/task"
    task = {"title": "Task2", "description": "Task2", "priority": "0", "status": "NEW"}
    headers = {'Content-Type': 'application/json'}
    db_param = ["""dbname = 's_ink' user = 'postgres' host = 'localhost' password = 'postgres'""",
                """select title,description,priority,status from task"""]
    result_restapi = test_get_task(url + "/list")
    test_create_task(url + "/create", task, headers)
    test_validate_json_data(task)
    database_values = test_database_fetch(db_param)
    test_validate_database_entries(database_values, task)
