import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import { fromEvent } from 'rxjs';
import { pairwise, switchMap, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-canvas',
  template: '<canvas #canvas></canvas>',
  styles: ['canvas {border: 1px solid #000;}']
})
export class CanvasComponent implements AfterViewInit, OnInit, OnChanges {
  @ViewChild('canvas') public canvas: ElementRef | undefined;

  @Input() public width = 400;
  @Input() public height = 400;
  @Input() public color;

  @Output()
  sendCoordinatesEvent: EventEmitter<any> = new EventEmitter<any>();

  disabled = true;
  public cx: CanvasRenderingContext2D | null | undefined;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.cx && changes['color']?.currentValue) {
      this.cx.strokeStyle = changes['color'].currentValue;
    }
  }

  ngOnInit(): void {
  }

  fillCanvas() {
    if (this.cx) {
      this.cx.fillRect(0, 0, 1200, 600);
    }
  }

  public ngAfterViewInit() {
    const canvasEl: HTMLCanvasElement = this.canvas?.nativeElement;

    this.cx = canvasEl.getContext('2d');

    canvasEl.width = this.width;
    canvasEl.height = this.height;

    if (!this.cx) throw 'Cannot get context';

    this.cx.lineWidth = 10;
    this.cx.lineCap = 'round';
    this.cx.strokeStyle = this.color;
    this.cx.fillStyle = 'white';
    this.fillCanvas();
    this.captureEvents(canvasEl);
  }

  public refresh() {
    if (!this.cx) throw 'Cannot get context';
    this.cx.strokeStyle = 'red';
  }


  private captureEvents(canvasEl: HTMLCanvasElement) {
    fromEvent(canvasEl, 'mousedown')
      .pipe(
        switchMap(e =>
          fromEvent(canvasEl, 'mousemove').pipe(
            takeUntil(fromEvent(canvasEl, 'mouseup')),
            takeUntil(fromEvent(canvasEl, 'mouseleave')),
            pairwise()
          )
        )
      )
      .subscribe((res) => {
        const rect = canvasEl.getBoundingClientRect();
        const prevMouseEvent = res[0] as MouseEvent;
        const currMouseEvent = res[1] as MouseEvent;

        const prevPos = {
          x: prevMouseEvent.clientX - rect.left,
          y: prevMouseEvent.clientY - rect.top
        };

        const currentPos = {
          x: currMouseEvent.clientX - rect.left,
          y: currMouseEvent.clientY - rect.top
        };
        const px = prevMouseEvent.clientX - rect.left;
        const py = prevMouseEvent.clientY - rect.top;

        const cx = currMouseEvent.clientX - rect.left;
        const cy = currMouseEvent.clientY - rect.top;

        this.sendCoordinatesEvent.emit({
          startX: px,
          startY: py,
          endX: cx,
          endY: cy,
          color: 'red'
        })
        console.log(Number(px));
        console.log(prevPos, currentPos);
      });

  }

  public drawOnCanvas(px: number, py: number, cx: number, cy: number): void {
    if (!this.cx) {
      return;
    }

    this.cx.beginPath();
    const prevPos = {
      px,
      py
    };

    if (prevPos) {
      this.cx.moveTo(px, py);
      this.cx.lineTo(cx, cy);
      this.cx.stroke();
    }
  }

}



