/*
 * Copyright 2020 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect

import cats.implicits._

trait SyncManaged[R[_[_], _], F[_]] extends Sync[R[F, *]] with Region[R, F, Throwable] {
  type Case[A] = Either[Throwable, A]

  def CaseInstance = catsStdInstancesForEither[Throwable]

  def to[S[_[_], _]]: PartiallyApplied[S]

  trait PartiallyApplied[S[_[_], _]] {
    def apply[A](rfa: R[F, A])(implicit S: Sync[S[F, *]] with Region[S, F, Throwable]): S[F, A]
  }
}

object SyncManaged {
  def apply[R[_[_], _], F[_]](implicit R: SyncManaged[R, F]): R.type = R
}
