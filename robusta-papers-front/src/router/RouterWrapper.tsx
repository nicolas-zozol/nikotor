import React from 'react';

import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';
import {Jumbo} from '../home/Jumbo';
import {Presentation} from '../home/Presentation';
import {Login} from '../account/Login';
import {Register} from '../account/Register';
import {faUser} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {TopBar} from '../page/TopBar';
import {ApplicationContext} from '../ApplicationContext';

export function RouterWrapper() {
  return (
    <Router>
      <ApplicationContext>
        <div>
          <TopBar />

          {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
          <Switch>
            <Route path="/login">
              <Login />
            </Route>
            <Route path="/register">
              <Register />
            </Route>
            <Route path="/">
              <Jumbo />
              <Presentation />
            </Route>
          </Switch>
        </div>
      </ApplicationContext>
    </Router>
  );
}
