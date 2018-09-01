#!/bin/zsh

source "${0:a:h}/.common.sh"

function initiate-tmux() {
  # Initiate with TmuxSessionName
  new-session;

  MainW=$TmuxSessionName:0
  set-option $MainW allow-rename off > /dev/null;
  splitwh-setup $MainW '.' \
      'sbt' \
      'git status' \
  ;
  select-pane $MainW -L; send-to $MainW Enter;
  select-pane $MainW -R; send-to $MainW Enter;
  rename-window $MainW 'root';

  EnumzJVMW=$TmuxSessionName:1
  new-window-splitwh-setup $EnumzJVMW 'modules/enumz' 'EnumzJVM';
  select-pane $EnumzJVMW -L;
  send-to $EnumzJVMW \
      '../..' Enter \
      'sbt' Enter \
      'project enumzJVM' Enter \
  ;
  select-pane $EnumzJVMW -R;

  EnumzJSW=$TmuxSessionName:2
  new-window-splitwh-setup $EnumzJSW 'modules/enumz' 'EnumzJS';
  select-pane $EnumzJSW -L;
  send-to $EnumzJSW \
      '../..' Enter \
      'sbt' Enter \
      'project enumzJS' Enter \
  ;
  select-pane $EnumzJSW -R;

  select-window $MainW;
}

if ! is-initiated; then
  initiate-tmux
fi

attach-tmux
