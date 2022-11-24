import {EventEmitter, Injectable} from '@angular/core';
import {Stomp} from "@stomp/stompjs";
import * as SockJS from "sockjs-client";
import { BehaviorSubject, Observable } from "rxjs";
import { User } from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class StompService {

  public stompClient;

  public onConnect: EventEmitter<void> = new EventEmitter<void>();

  public winner: BehaviorSubject<string> = new BehaviorSubject<string>(null);
  public winnerObservable: Observable<string> = this.winner.asObservable();

  public resultList: BehaviorSubject<User[]> = new BehaviorSubject<User[]>(null);
  public resultListObservable: Observable<User[]> = this.resultList.asObservable();

  constructor() {
  }

  connect(name: string) {
    const socket = new SockJS('http://localhost:8080/stomp-endpoint');
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({},  (frame: string) => {
      console.log('Connected: ' + frame);
      this.onConnect.emit();

    });
  }

  disconnect() {
    sessionStorage.removeItem('user');
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    console.log('Disconnected!');
  }

}
