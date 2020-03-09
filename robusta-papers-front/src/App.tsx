import React, {useState} from 'react';
import './App.css';
import {Jumbo} from './home/Jumbo';
import {Presentation} from './home/Presentation';
import {OptionalUser, UserContext} from "./account/UserContext";
import {TopBar} from "./page/TopBar";
import {Route, Switch} from "react-router";
import {Login} from "./account/Login";
import {Register} from "./account/Register";
import {BrowserRouter as Router} from "react-router-dom";

function App() {

    const [loggedUser, setLoggedUser] = useState<OptionalUser>(undefined)
    const [justRegistered, setJustRegistered] = useState(false);

    return (
        <>
            <UserContext.Provider value={loggedUser}>

                <Router>
                    <div>
                        <TopBar/>

                        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
                        <Switch>
                            <Route path="/login">
                                <Login updateLoggedUser={setLoggedUser}/>
                            </Route>
                            <Route path="/register">
                                <Register onRegister={() => setJustRegistered(true)}/>
                            </Route>
                            <Route path="/">
                                <Jumbo/>
                                {justRegistered ? "Thank you, check you mail" : <Presentation/>}
                            </Route>
                        </Switch>
                    </div>
                </Router>
            </UserContext.Provider>
        </>
    );
}

export default App;
