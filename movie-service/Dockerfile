FROM golang:1.23.2-alpine3.20 AS builder

RUN go env -w GOPROXY=direct
RUN apk add --no-cache git
RUN apk --no-cache add ca-certificates && update-ca-certificates

WORKDIR /src

COPY ./go.mod ./go.sum ./

RUN go mod download

COPY internal internal
COPY cmd cmd

RUN go install ./...

RUN CGO_ENABLE=0 go build \
    -installsuffix 'static' \
    -o /movie-ms ./cmd/main.go

FROM alpine:3.20.3 AS runner

WORKDIR /usr/bin

COPY --from=builder ./movie-ms .

EXPOSE 8081

CMD [ "./movie-ms" ]
