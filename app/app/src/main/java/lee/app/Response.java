package lee.app;

public class Response<T> {
    public T data;

    public Response(T data) {
        this.data = data;
    }

    static class Success<T> extends Response<T> {
        public Success(T data) {
            super(data);
        }
    }

    static class Failure<T> extends Response<T> {
        public Failure() {
            super(null);
        }
    }

    static class NotExist<T> extends Response<T> {
        public NotExist() {
            super(null);
        }
    }

    static class AlreadyExist<T> extends Response<T> {
        public AlreadyExist() {
            super(null);
        }
    }
}
