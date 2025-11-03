---
layout: default.md
title: "Developer Guide"
pageNav: 3
---
# BloodNet Developer Guide

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

- The blood donation eligibility criteria implemented in this project were guided by the [Health Sciences Authority (HSA)](https://www.hsa.gov.sg/blood-donation/can-i-donate) guidelines in Singapore.

## Table of Contents
- [Setting up, getting started](#setting-up-getting-started)
- [Design](#design)
  - [Architecture](#architecture)
  - [UI Component](#ui-component)
  - [Logic Component](#logic-component)
  - [Model Component](#model-component)
  - [Storage Component](#storage-component)
  - [Common Classes](#common-classes)
- [Implementation](#implementation)
  - [User Confirmation](#user-confirmation)
- [Future Implementation](#future-implementation)
- [Documentation, Logging, Testing, Configuration, Dev-ops](#documentation-logging-testing-configuration-dev-ops)
- [Appendix: Requirements](#appendix-requirements)
- [Product Scope](#product-scope)
- [User Stories](#user-stories)
- [Use Cases](#use-cases)
  - [UC01 - Add a donor's contact & blood type](#use-case-uc01-add-a-donor-s-contact-and-amp-blood-type)
  - [UC02 - Update a donor's information](#use-case-uc02-update-a-donor-s-information)
  - [UC03 - Search donors by name](#use-case-uc03-search-donors-by-name)
  - [UC04 - Delete a donor](#use-case-uc04-delete-a-donor)
  - [UC05 - List all donors in the system](#use-case-uc05-list-all-donors-in-the-system)
  - [UC06 - Find all eligible donors of a particular blood type](#use-case-uc06-find-all-eligible-donors-of-a-particular-blood-type)
  - [UC07 - Record a blood donation by a donor](#use-case-uc07-record-a-blood-donation-by-a-donor)
  - [UC08 - List all blood donations by a donor](#use-case-uc08-list-all-blood-donations-by-a-donor)
  - [UC09 - Modify a blood donation record](#use-case-uc09-modify-a-blood-donation-record)
  - [UC10 - Delete a blood donation record](#use-case-uc10-delete-a-blood-donation-record)
- [Non-Functional Requirements](#non-functional-requirements)
- [Glossary](#glossary)
- [Appendix: Instructions for Manual Testing](#appendix-instructions-for-manual-testing)
  - [Launch and Shutdown](#launch-and-shutdown)
  - [Instructions](#instructions-to-test)
    - [find](#find)
    - [list](#list)
    - [add](#add)
    - [edit](#edit)
    - [delete](#delete)
    - [finddonations](#finddonations)
    - [adddonation](#adddonation)
    - [editdonation](#editdonation)
    - [deletedonation](#deletedonation)
    - [findeligible](#findeligible)
    - [clear](#clear)
    - [help](#help)
    - [exit](#exit)
  - [Appendix: Effort](#appendix-effort)
  - [Appendix: Planned Enhancements](#appendix-planned-enhancements)
--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** shown above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-T16-4/tp/tree/master/src/main/java/bloodnet) and [`MainApp`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/java/bloodnet/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The input handler.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1` followed by responding with `yes` when user confirmation is sought before the command is executed.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the above diagram):

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/java/bloodnet/ui/Ui.java).

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`InputBox`, `OutputDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/java/bloodnet/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/resources/view/MainWindow.fxml).

The `UI` component:

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/java/bloodnet/logic/Logic.java)

Here is a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="650"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `handle("delete 1")` API call followed by a `handle("yes")` API call as an example in a freshly started program.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:
1. Receive input
* The `Logic` is called upon to handle an input
2. Check for active session
* `Logic` checks for an active current session
* If there is no active current session:
    * The input is passed to a `BloodNetParser` object which in turns creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
    * This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`), which its `createSession` is invoked by `LogicManager`  to create a new `CommandSession` object (more precisely, an object of one of its subclasses e.g., `ConfirmationCommandSession`), which will become `LogicManager`'s `currentCommandSession`.
        * During the invoking of `createSession`, the `Command` object (depending on its implementation of `createSession`) may interact with the `Model` component to query target objects and/or perform validation checks.
3. Advance current session
* The current session is called upon to handle the input.
* The result of the input handling is encapsulated as an `InputResponse` object.
* If the current command session has finished (as checked by its `isDone` method), the current session will be marked as `null`.
* The `InputResponse` object is then returned back from `Logic`.

#### Parsing
Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `BloodNetParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `BloodNetParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

#### Command Sessions
`CommandSession` is an abstraction that has been developed to manage user interactivity during a command lifecycle in a maintainable and extensible way. It encapsulates the state and logic needed to handle multi-step (which includes single-step) interactions, while keeping the `Command` execution logic separate from input handling. Since `CommandSession` does not implement the `Command`'s execution logic itself, this logic is typically passed in during construction either by providing the `Command` object itself or by wrapping it in a help object (e.g., `DeferredExecution`).

By using sessions, the system can:
* Pause a command mid-lifecycle to wait for user input
* Maintain contextual information across multiple user inputs

To make the handling of user inputs (regardless of whether it is a command input or session input) uniform, **all commands create a `CommandSession` via `Command#createSession(Model model)**, regardless of whether they are interactive or single-step:
* **Interactive commands** (e.g., `delete`) creates a specialised session like `ConfirmationCommandSession` that manage multi-step interactions
* **Single-step commands** (e.g., `list`) creates a `SingleStepCommandSession` which immediately carry out the command's execution. This is the default behaviour of a `Command` if `Command#createSession(Model)` is not overridden.

This design allows the `LogicManager` to **treat all user inputs uniformly**, using the presence or absence of a `currentCommandSession` to determine whether an input should be treated as a new command input.

The method `CommandSession#isDone()` is then used by `LogicManager` to determine whether a session has completed. Once it returns `true`, the session is cleaned up, clearing `currentCommandSession` and allowing the next command input to be processed.

The following activity diagram summarises the session lifecycle management when the user inputs something:
<puml src="diagrams/CommandSessionActivityDiagram.puml" width="600"/>

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/master/src/main/java/bloodnet/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component:

* stores the BloodNet data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-T16-4/tp/blob/12e2e4ca39a1f9106499633fe0d58a6ad3996260/src/main/java/bloodnet/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component:
* can save both BloodNet data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `BloodNetStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save or retrieve objects that belong to the `Model`).

### Common classes

Classes used by multiple components are in the `bloodnet.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### User Confirmation
The user confirmation mechanism is facilitated by `ConfirmationCommandSession` class which is an implementation of `CommandSession` (See [Command Sessions](#command-sessions) for more details.)

The `ConfirmationCommandSession` class manages interactive commands that require explicit user confirmation before execution (i.e., destructive operations).
* When a command input is identified as requiring confirmation, the `LogicManager` invokes the parsed `Command`'s `createSession()` method, producing a `ConfirmationCommandSession` that stores the execution of the Command in a `deferredExecution` object.

To facilitate the handling of inputs within the context of its session, `ConfirmationCommandSession` maintains three internal states:

| State                  | Description                                                                                                                                                                                                  | Transition Condition                                     |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|
| `INITIAL`              | Default state immediately after creation. Ignores the original command input (since its information is already encapsulated within the deferred execution passed into it) and returns a confirmation prompt. | Automatically transitions to `PENDING_CONFIRMATION`.     |
| `PENDING_CONFIRMATION` | Waits for user input(`yes` or `no` (caps-insensitive)) <br><ul><li> `yes` -> carry out deferred execution of command.</li><li>`no` -> cancels command</li><li>Other input -> re-prompts user.                | Transitions to `DONE` after confirmation or cancellation |
| `DONE`                 | Terminal state indicating the session has completed. Any further `handle()` calls throw `TerminalSessionStateException`                                                                                      | -                                                        |

The following activity diagrams contrast the flow of a command requiring user confirmation and a single-step command:
<puml src="diagrams/ConfirmationCommandSessionActivityDiagram.puml" alt="ConfirmationCommandSessionActivityDiagram"/>

<puml src="diagrams/SingleStepCommandSessionActivityDiagram.puml" alt="SingleStepCommandSessionActivityDiagram"/>

For clarity, the above diagrams omit general session handling, command parsing and input delegation. See [**Command Sessions**](#command-sessions) for a complete overview.

--------------------------------------------------------------------------------------------------------------------

## **Future Implementation**

In the future, we can make the `FilteredPersonList` and `FilteredDonationRecordList` synchronized. This means that the `DonationRecords` displayed correspond to the `Persons` displayed at all times. For instance, when the user finds eligible donors from the `PersonList`, the `DonationRecordList` will be filtered such that only records which correspond to the displayed `Persons` are displayed to the user. By doing so, the approach ensures that both the `PersonList` and the `DonationRecord` lists are always consistent. 

The diagram below illustrates a potential implementation using the `FindEligible` command as an example.

<puml src="diagrams/FutureUIImplementationForFindEligible.puml" alt="FutureUIImplementationForFindEligible"/>

__________________________________________________________________________________________________

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a database of donors and their donation records
* requires quick access to donor personal information
* may need to find donors of a particular blood type who are eligible for donation on the current day
* prefer desktop apps over other types of apps
* is able to type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: manage donor profiles more efficiently as opposed to a typical mouse driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …      | I want to …                                                      | So that …                                                                                                     |
|----------|-------------|------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| * * *    | admin staff | add a donor’s contact & blood type                               | the blood bank can keep in touch with the donor if more information is needed                                 |
| * * *    | admin staff | add a donor’s date of birth                                      | the blood bank knows can determine a donor's eligibility in donating blood                                    |
| * * *    | admin staff | search donors by name                                            | I can find their contact information if I need to contact them                                                |
| * * *    | admin staff | modify a donor’s contact information                             | I can fix the stored contact information if it was keyed in wrongly previously                                |
| * * *    | admin staff | modify a donor’s date of birth                                   | I can fix the stored date of birth if it was keyed in wrongly                                                 |
| * * *    | admin staff | modify a donor’s blood type                                      | I can fix the stored blood type if it was keyed in wrongly previously                                         |
| * * *    | admin staff | delete a donor if they have no donation records                  | I can remove donors who were accidentally added to the system                                                 |
| * * *    | admin staff | list all donors in the system                                    | I can have a quick overview of all the donors who have agreed to donate blood to us                           |
| * * *    | admin staff | record a blood donation by a donor                               | I can track how many donations each contact has made and the details of those donations                       |
| * * *    | admin staff | modify a blood donation record                                   | I can modify wrongly keyed in donation records                                                                |
| * * *    | admin staff | delete a blood donation record                                   | I can remove wrongly keyed in donation records                                                                |
| * * *    | admin staff | find all eligible donors given a blood type                      | I can determine who I can call if blood is needed                                                             |
| * *      | admin staff | detect duplicate donation records associated with the same donor | I am able to quickly identify duplicate data in the BloodNet system and reconcile it to reduce data pollution |
| *        | admin staff | record how much blood was donated by a donor in a session        | I can recommend donors who have been very active for appreciation awards, to incentivise more donors          |


### Use cases

(For all use cases below, the **System** is `BloodNet`, unless specified otherwise.)

---

### **Use case: UC01 - Add a donor’s contact & blood type**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to add a donor and provides the donor's information.
2. BloodNet creates a new donor entry with the provided details.

Use case ends.

**Extensions**

* 1a. One or more fields of the provided information is invalid
    * 1a1. BloodNet shows an error message.
    * Use case returns to step 1.

---

### **Use case: UC02 - Update a donor's information**

**Actor**: Admin staff

**MSS**

1. Admin staff lists all donors ([UC05](#use-case-uc05-list-all-donors-in-the-system)). 
2. Admin staff selects a donor to update and provides new values for the fields that need to be changed. 
3. BloodNet updates fields of the donor for which values were supplied.

Use case ends.

**Extensions**
* 1a. The list is empty.
    * Use case ends.

* 2a. Donor Index not found.
    * 2a1. BloodNet shows an error message.
    * Use case returns to step 2.

* 2b. One or more invalid values provided.
    * 2b1. BloodNet shows an error message.
    * Use case returns to step 2.

---

### **Use case: UC03 - Search donors by name**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find a donor by their name.
2. BloodNet searches and displays donors with matching names.

Use case ends.

---

### **Use case: UC04 - Delete a donor**

**Actor**: Admin staff

**MSS**

1. Admin staff lists all donors ([UC05](#use-case-uc05-list-all-donors-in-the-system)).
2. Admin staff requests to delete a donor.
3. BloodNet requests for confirmation.
4. Admin staff confirms deletion.
5. BloodNet deletes a donor.

Use case ends.

**Extensions**

* 1a. The list is empty.
    * Use case ends.

* 2a. Donor Index is invalid.
    * 2a1. BloodNet shows an error message.
    * Use case returns to step 2.

* 2b. Donor has one or more existing donation records.
    * 2b1. Admin staff deletes donation records of donor ([UC10](#use-case-uc10-delete-a-blood-donation-record)).
    * Use case returns to step 2.

* 3a. Admin staff declines deletion.
    * 3a1. BloodNet cancels the deletion request.
    * Use case ends.

---

### **Use case: UC05 - List all donors in the system**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to list all donors.
2. BloodNet displays a list of all non-deleted donors.

Use case ends.

---

### **Use case: UC06 - Find all eligible donors of a particular blood type**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find donors of a particular blood type.
2. BloodNet searches the entire donor list and applies the eligibility rules, such as date of birth and days since last donation.
3. BloodNet displays all donors who match the given blood type and the eligibility rules.

Use case ends.

**Extensions**

* 1a. Invalid blood type entered.
    * 1a1. BloodNet shows an error message.
    * Use case relates back to step 1, prompting the admin staff to re-enter a blood type.

---

### **Use case: UC07 - Record a blood donation by a donor**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find a donor by their name.
2. BloodNet searches and displays donors with matching names.
3. Admin staff requests to record a blood donation by a donor.
4. BloodNet records the donation details, including date and volume.

Use case ends.

**Extensions**

* 2a. No donors match the search name.
    * Use case ends.

* 3a. Donor Index is invalid.
    * 3a1. BloodNet shows an error message.
    * Use case returns to step 3.

* 3b. Date or volume format is invalid.
    * 3b1. BloodNet shows an error message.
    * Use case resumes at step 3.

---

### **Use case: UC08 - List all blood donations by a donor**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find a donor by their name.
2. BloodNet searches and displays donors with matching names.
3. Admin staff requests to list all blood donations by a specified donor.
4. BloodNet returns a list of blood donations, sorted by donation date in descending order.

Use case ends.

**Extensions**

* 2a. No donors match the search name.
    * Use case ends.

* 3a. Donor Index is invalid.
    * 3a1. BloodNet shows an error message.
    * Use case returns to step 3.

---

### **Use case: UC09 - Modify a blood donation record**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find a donor by their name.
2. BloodNet searches and displays donors with matching names.
3. Admin staff requests to list all blood donations by a specified donor.
4. BloodNet returns a list of blood donations, sorted by donation date in descending order.
5. Admin staff requests to update a specified blood donation record.
6. BloodNet updates the donation record.

Use case ends.

**Extensions**

* 2a. No donors match the search name.
    * Use case ends.

* 3a. Donor Index is invalid.
    * 3a1. BloodNet shows an error message.
    * Use case returns to step 3.

* 5a. Donation Record Index is invalid.
    * 5a1. BloodNet shows an error message.
      Use case returns to step 5.

* 5b. Invalid data format for date or volume.
    * 5b1. BloodNet shows an error message.
      Use case returns to step 5.

---

### **Use case: UC10 - Delete a blood donation record**

**Actor**: Admin staff

**MSS**

1. Admin staff requests to find a donor by their name.
2. BloodNet searches and displays donors with matching names.
3. Admin staff requests to list all blood donations by a specified donor.
4. BloodNet returns a list of blood donations, sorted by donation date in descending order.
5. Admin staff requests to delete a specified blood donation record.
6. BloodNet requests for confirmation.
7. Admin staff confirms deletion.
8. BloodNet deletes the donation record.

Use case ends.

**Extensions**

* 2a. No donors match the search name.
    * Use case ends.

* 3a. Donor Index is invalid.
    * 3a1. BloodNet shows an error message.
    * Use case returns to step 3.

* 5a. Donation Record Index is invalid.
    * 5a1. BloodNet shows an error message.
    * Use case returns to step 5.

* 6a. Admin staff declines deletion.
    * 6a1. BloodNet cancels the deletion request.
    * Use case ends.

---


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons and the program should still be responsive with response time less than 1 second for each operation.
3.  A user with typing speed of more than 40 words per minute for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands rather than using the mouse.
4. Should start within 5 seconds on a typical user machine (4 core CPU, 8GB RAM, SSD).
5. User guide should be written with easy-to-understand English that is comprehensible to users without technical background.
6. The user interface should be intuitive enough for users who are not IT-savvy.
7. Should not be required to have beautiful visual design or animations since it is for administrative use.
8. Should have user confirmation for _destructive operations_.
9. Should provide error message, in response to an invalid operation, that details what went wrong and how to fix it for a non-technical user.
10. Should be designed for a single user within a single computer.
11. Ensure contacts data persists in a human-editable file; manual editing errors are the user’s responsibility.
12. Should work without requiring an installer.
13. Should be built into a JAR file of no larger than 100MB.


### Glossary

* **Blood Type**: The blood types supported are A+, A-, B+, B-, AB+, AB-, O+, O-
* **Donor**: Person who donates blood to others
* **Destructive operation**: An action that leads to permanent removal of data
* **Field**: A single piece of information for a donor or donation record.
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: An 8-digit Singaporean (+65) phone number that is not meant to be shared with others
* **Run**: In the context of a command, refers to carrying out the entire lifecycle of a command, including user interaction and invoking domain logic
* **Execution**: In the context of a command, refers specifically to invoking the domain logic of the command, without handling any user interaction
* **Input Box**: The text box in the application that receives all textual inputs from the user
* **Output Display**: The text box in the application that displays output resulting from processing an input/ executing a command
* **User Input**: Any textual input entered by the user into the input box
* **Command Input**: A specific type of user input that triggers a new command to run
* **Input Response**: The application's response to a user input, encapsulating information such as the output to display in the output display and whether to exit the application
* **Command Result**: A specific type of input response produced by a command after executing its domain logic
* **CPU**: The component in charge of executing program instructions and processing command logic within the application
* **SSD**: The device that stores the application's data files and where user and system data are kept
* **RAM**: The memory used by the application to temporarily store data and other information while the commands are being executed, thereby enabling fast access alongside processing
* **CLI**: A text-based interface that allows users to interact with a program by typing commands

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Instructions to Test
#### find:
##### _Test 1_
* **Test instructions**: Run `find aleX davId iRfan`.

* **Expected Message**: “3 donors listed!”

* **Expected Displayed Donor List**: Displays Alex Yeoh, David Li and Irfan Ibrahim.

* **Expected Displayed Donation Records List**: No change.



#### list:
##### _Test 1_
* **Test instructions**: Run `list`.

* **Expected Message**: “Listed all donors”

* **Expected Displayed Donor List**: Display all 6 donors.

* **Expected Displayed Donation Records List**: No change.



#### add:
##### _Test 1_
* **Test instructions**: Run `add n/John Doe p/98765432 e/johnd@example.com b/A+ d/30-03-2004`.

* **Expected Message**: “New donor added: John Doe; Phone: 98765432; Email: johnd@example.com; Blood Type: A+; Date Of Birth: 30-03-2004”

* **Expected Displayed Donor List**: John Doe is added to the end of the list.

* **Expected Displayed Donation Records List**: No change.



#### edit:
##### _Test 1_
* **Test instructions**: Run `list`, followed by `edit 1 p/91234567 e/johndoe@example.com`.

* **Expected Message**: “Edited donor: Alex Yeoh; Phone: 91234567; Email: johndoe@example.com; Blood Type: A+; Date Of Birth: 28-03-1995”

* **Expected Displayed Donor List**: Alex Yeoh’s phone number is updated to “91234567”, while his email is updated to “johndoe@example.com”.

* **Expected Displayed Donation Records List**: No change.



#### delete:
##### _Test 1_
* **Test instructions**: Run `list`, followed by `delete 6`.

* **Expected Message**: “Are you sure you want to delete Roy Balakrishnan? This action is not reversible.
  Key in either 'yes' or 'no'.”

* **Expected Displayed Donor List**: No change.

* **Expected Displayed Donation Record List**: No change.

* **Test instructions (cont.)**: Then, input `yes`.

* **Expected Message**: “Deleted donor: Roy Balakrishnan; Phone: 92624417; Email: royb@example.com; Blood Type: O+; Date Of Birth: 21-03-2004”

* **Expected Displayed Donor List**: Roy Balakrishnan is removed from the donor list.

* **Expected Displayed Donation Record List**: No change.



#### finddonations:
##### _Test 1_
* **Test instructions**: Run `list`, followed by `finddonations 1`

* **Expected Message**: “2 donation records related to Alex Yeoh found!”

* **Expected Displayed Donor List**: No change.

* **Expected Displayed Donation Record List**: Displays only two donation records and the donor of these records is Alex Yeoh.

#### adddonation:
##### _Test 1_
* **Test instructions**: Run `list`, followed by `adddonation p/2 d/10-10-2025 v/250`.

* **Expected Message**: “New donation record added: Donor Name: Bernice Yu; Donation Date: 10-10-2025; Blood Volume: 250"

* **Expected Displayed Donor List**: No change.

* **Expected Displayed Donation Record List**:  Displays only one donation record and the donor of these records is Bernice Yu.



#### editdonation:
##### _Test 1_
* **Test instruction**: Run `finddonations 1`, followed by `editdonation 2 v/499`.

* **Expected Message**: “Edited Donation Record: Donor Name: Alex Yeoh; Donation Date: 15-10-2025; Blood Volume: 499”

* **Expected Displayed Donor List**: No change to the donor list.

* **Expected Display Donation Record List**: The 2nd donation record shown with the name “Alex Yeoh” should have the changed blood volume from 450 ml to 499 ml.



#### deletedonation:
##### _Test 1_
* **Test instructions**: Run `finddonations 3`, followed by `deletedonation 1`.

* **Expected Message**: “Are you sure you want to delete donation record for: Donor Name: Charlotte Oliveiro; Donation Date: 21-03-2025; Blood Volume: 400? This action is not reversible.
  Key in either 'yes' or 'no'.”

* **Expected Displayed Donor List**: No change.

* **Expected Displayed Donation Record List**: No change.

* **Test instructions (cont.)**: Then, key in “yes”

* **Expected message**: “Deleted Donation Record: Donor Name: Charlotte Oliveiro; Donation Date: 21-03-2025; Blood Volume: 400”

* **Expected Displayed Donor List**: No change

* **Expected Displayed Donation Records List**: The 1st record should have been deleted.



#### findeligible:
##### _Test 1_
* **Test instructions**: Run `findeligible A+`.

* **Expected message**: "0 donors listed!"

* **Expected Displayed Donor List**: Empty donor list.

* **Expected Displayed Donation Records List**: No change.


##### _Test 2_
* **Test instructions**: Run `findeligible B+ AB+`.

* **Expected message**: "2 donors listed!"

* **Expected Displayed Donor List**: Displays Bernice Yu and David Li.

* **Expected Displayed Donation Records List**: No change.



#### clear:
##### _Test 1_
* **Test instructions**: Run `list` followed by `clear`.

* **Expected Message**: “Are you sure you want to clear BloodNet? This action is not reversible.
  Key in either 'yes' or 'no'.”

* **Expected Displayed Donor List**: No change.

* **Expected Displayed Donation Records List**: No change.

* **Test instructions (cont.)**: Then, input `yes`.

* **Expected Message**: “BloodNet has been cleared!”

* **Expected Displayed Donor List**: Empty donor list.

* **Expected Displayed Donation Records List:** Empty donation records list.



#### help:
* **Test instructions**: Run `help`.

* **Expected Action**:  A help window appears on the screen. The help window should be resizable and show a list of all of the commands with usage instructions.



#### exit:
* **Test instructions**: Run `exit`.

* **Expected Action**:  The program closes the graphical user interface and automatically saves all current data before exiting.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

In the course of the past few weeks, beyond just repurposing AB3 to a blood donor address book, we have made significant upgrades to it for our target audience, which has involved a substantial amount of hard work, ingenuity, late nights and early mornings (just look at some of the commit timings):

### New command to find eligible donors of particular blood types.
When reserves of a particular blood type are running low, users may want to search for existing donors in the system who are eligible to donate, so they can reach out to them and request for an urgent donation. This was challenging as the official rules on blood donation eligibility are fairly complex. It should be noted that an eligibility check is conducted when adding or editing a donation record.

### New Donation Record entity type
In order for the system to not only track donors but also their donations (i.e: when and what volume of blood in millilitres was donated), we added a Donation Record model. This was incredibly time-consuming due to the increase in complexity in having multiple entity types compared to AB3 which only deals with one entity type.

#### New commands to manage donation records
To manage the donation records, we added new commands to add, edit, delete, and find donation records, which took time to create, debug and document.

### User confirmation
Wanting to safeguard against accidental destructive operations, we sought to implement user confirmation before such operations.

To accomplish this, much complexity needed to be introduced. AB3 originally executes every user input as a new command immediately. But introducing user interactivity within a command (which user confirmation support requires) fundamentally change this flow requiring us to create a new abstraction, `commandSession`, to manage multi-step interactions and persist information throughout the command lifecycle until completion.

The implementation was challenging due to input delegation, differentiating between a new command input and an input within a command session, handling command exceptions resulting in mid-session exits, and maintaining consistent system state, all while providing a uniform framework compatible with single-step commands. Documentation also required careful revision as existing terms like "command", “execution” needed to be clarified and properly redesigned.

Overall, the addition of this new feature involved considerable architectural changes, edge case handling and documentation effort to balance the usability and safety of the system.

### New Blood Type and Date of Birth fields (Person model)
In order for the person model to capture the information users need to track each donor, we added the blood type and date of birth fields. This meant that we had to add additional lines of code in many places of the codebase.

## **Appendix: Planned Enhancements**

Team size: 5

1. **Feature**: Allow users to delete a donor along with all donation records associated with that specific 
   donor in a single step. <br>

   **Current Behaviour**: Users must manually delete all donation records with respect to a donor before they are able to delete a donor.
   This is a feature not a bug, because in real life, users would most likely want to preserve a donor's donation records in the system for audit purposes, even if the donor is past the age of eligibility. Therefore, we have intentionally made deleting a donor and their donation records difficult.<br>

   **Rationality for the New Feature**: However, some PE-D testers have expressed that the current process is fairly
   time-consuming. As such, if real users express a similar sentiment too, we will introduce this new feature. <br><br>

2. **Feature**: Introduce a `listdonations` command that allows users to view all donation records in the system. <br>

   **Current Behaviour**: There is no feature that allows users to view all the donation records at once and users 
   are only allowed to access donation records individually through other commands such as `finddonations`. This design 
   choice was purely intentional as we believed that users did not see a need to view all donation records at once.
   Rather, our focus was more on allowing users to retrieve specific records efficiently, prioritizing quick access to 
   individual donation records versus displaying a full list of all donation records, which we believed would be 
   overwhelming or unnecessary for most users. <br>

    **Rationality for the New Feature**: Some PE-D testers have noticed that the absence of such a feature makes it difficult
     to get an overview of all the donation records prevalent in the system. As such, if real users express a similar need, we will introduce the `listdonations` feature. <br><br>

3. **Feature**: Warn users when BloodNet already contains a donor with the same phone number or the same name.<br>

   **Current Behaviour**: 2 donors are only considered to be identical if they both have the same name (case-sensitive) and phone number.<br>

    **Rationality for the New Feature**: It is possible for the existing validation check to miss certain edge cases. For example, if a donor with a name "Alex Tan" and phone number "88888888" already exists in BloodNet, a donor with a name "Alex T." and phone number "88888888" can still be added to the BloodNet, even though there is a decent chance they refer to the same person.<br><br>

4. **Feature**: Loosen phone number validation requirements.<br>

   **Current Behaviour**: The phone number must contain exactly 8 digits, but this may be too strict in some scenarios. For example, "8888 8888" would fail the validation check due to the whitespace in between.<br>

    **Rationality for the New Feature**: We would like for the validation to be more user-friendly by accepting common formatting characters (spaces, parenthesis, plus signs, dashes) <br><br>

5. **Feature**: Thicken panel borders to make resizing easier for users.<br>

   **Current Behaviour**: The margin between the panels is very small, thus resulting in users struggling to resize the panels, especially for the two lists. <br>

   **Rationality for the New Feature**: We would like to make our GUI more usable and accessible for users.<br><br>

6. **Feature**: Use different tokens for `adddonation` and `add` to clearly distinguish the person index from a phone number.<br>

   **Current Behaviour**: Currently, both the `adddonation` and the `add` command use the `p/` token when inputting a command. Unfortunately, some PE-D testers found this behaviour unintuitive.<br>

   **Rationality for the New Feature**: We would like to ensure that the command inputs are clear, intuitive and easy for users to understand.<br><br>

7. **Feature**: Set a minimum size or lock resizing for all panels to prevent content truncation.<br>
   
   **Current Behaviour**: Currently, if users resize one of the lists too much, then the other list, such as the `DonationRecord` list is truncated, making it difficult for users to view both lists.<br>

   **Rationality for the New Feature**: We would like to prevent content truncation and improve readability when the panels are resized.<br><br>



