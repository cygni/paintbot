import * as React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { GameScreen } from './game/GameScreen';
import WelcomeScreen from './welcome/WelcomeScreen';

export const Routes = () => (
  <Router>
    <Switch>
      <Route path={'/'} exact={true} component={WelcomeScreen} />
      <Route path={'/game'} exact={true} component={GameScreen} />
    </Switch>
  </Router>
);
