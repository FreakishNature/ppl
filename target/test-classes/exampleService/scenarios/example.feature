Feature: Example
  Scenario: Login scenario
    When Login with login:login pass:pass
    Then Expected to contain:expirationTime
    Then Expected to contain:token
    Then Expected response code:200

  Scenario Outline: Create project scenario
    When Login with login:login pass:pass
    When Create project name <name>, descr <descr>, targetSum <targetSum>
    Then Expected response code:<code>

  Examples:
    |name|descr | targetSum  | code|
    |name2|mydescr|72.54     | 201 |
    |3123 |dsad   |-40       | 409 |


  Scenario Outline: Get project scenario
    When Get project with name: <name>
    Then Expected to contain:<descr>
    Then Expected to contain: <targetSum>
    Then Expected response code:<code>
    Examples:
      |name|descr | targetSum  | code|
    |name2|mydescr|72.54     | 200 |

  Scenario: Delete project
    When Delete project with name: name2
    Then Expected response code:202


  Scenario: Fail login
    When Login with login:somelogin pass:somepass
    Then Expected response code:400
    Then Expected to contain:Invalid username or password